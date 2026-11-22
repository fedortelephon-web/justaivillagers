package com.justaivillagers.fabric;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.justaivillagers.core.LlmException;
import com.justaivillagers.core.VillagerAiService;
import com.justaivillagers.core.VillagerProfile;
import com.justaivillagers.core.WorldContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * Per-tick logic: which villagers speak up, who is fleeing, who was hit, and who talks to whom in
 * the evening.
 *
 * <p>Throttling lives in the service (bounded concurrency) and the per-villager maps are bounded.
 * Hits are detected through an attack hook rather than a cooldown heuristic, and the
 * O(villagers x entities) scan runs once per tick.
 */
public final class VillagerWatcher {

    private static final double NEARBY_RADIUS = 6.0;
    private static final double NEARBY_RADIUS_SQ = NEARBY_RADIUS * NEARBY_RADIUS;
    private static final double HOSTILE_RADIUS = 6.0;
    private static final double HOSTILE_RADIUS_SQ = HOSTILE_RADIUS * HOSTILE_RADIUS;
    private static final long MIN_TICKS_BETWEEN_TALK = 6000L;
    /** One mutter per crowd, not one per villager: without this the whole village spoke at once. */
    private static final long MIN_TICKS_BETWEEN_ANY_TALK = 200L;
    /** Silence right after a world load, so the first seconds are not a wall of chat. */
    private static final long JOIN_GRACE_TICKS = 200L;
    private static final int MAX_TRACKED = 256;

    private final Set<UUID> fleeing = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<UUID, Long> lastSpoke = new ConcurrentHashMap<>();
    /** Wall-clock time of the last hit reaction per villager; see {@code hurtCooldownMs}. */
    private final ConcurrentHashMap<UUID, Long> lastHurt = new ConcurrentHashMap<>();
    /**
     * Hits that were seen but not yet reacted to.
     *
     * <p>The attack callback fires before the damage is applied, so at that moment a fatal blow still
     * looks like a healthy villager. Waiting one tick lets the health actually drop, which is what
     * separates "ouch" from "the villager is dead and should stay silent".
     */
    private final java.util.List<PendingHurt> pendingHurt = new java.util.ArrayList<>();

    private static final class PendingHurt {
        final VillagerEntity villager;
        final String lang;

        PendingHurt(VillagerEntity villager, String lang) {
            this.villager = villager;
            this.lang = lang;
        }
    }
    private final SpeechQueue speech = new SpeechQueue();
    private long tickCounter;
    /** Tick of the last spontaneous line by anyone; {@code -1} until the first one. */
    private long lastAnyTalk = -1L;

    public void reset() {
        fleeing.clear();
        lastSpoke.clear();
        lastHurt.clear();
        pendingHurt.clear();
        speech.clear();
        tickCounter = 0;
        lastAnyTalk = -1L;
    }

    /** Called every client tick. */
    public void tick(MinecraftClient client) {
        if (client == null || client.world == null || client.player == null) {
            return;
        }
        // Pump chat output before the screen check: dialog answers arrive while the dialog
        // screen is open, so an early return here would swallow them.
        speech.tick(client);
        // Resolve hits even while a screen is open: a villager killed by the server must not be
        // resurrected by a reaction that was queued a tick earlier.
        resolvePendingHurts(client);

        if (client.currentScreen != null) {
            return;
        }
        tickCounter++;
        cleanMaps();
        ModRuntime runtime = ModRuntime.get();
        var config = runtime.config();
        String lang = GameBridge.language(config.languageOverride);

        List<VillagerEntity> villagers = collectVillagers(client);
        if (villagers.isEmpty()) {
            return;
        }

        boolean night = isNight(client);
        if (config.enableEventReactions) {
            checkFleeing(client, villagers, lang, runtime);
        }
        if (config.enableSpontaneousTalk && !night && tickCounter % 20 == 0) {
            maybeSpontaneous(client, villagers, lang, runtime, config);
        }
        if (config.enableGroupDialogues && isEvening(client)) {
            maybeGroupDialogue(client, villagers, lang, runtime);
        }
    }

    /** Keeps the per-villager maps bounded without a full clear on every tick. */
    private void cleanMaps() {
        if (lastSpoke.size() > MAX_TRACKED) {
            lastSpoke.clear();
        }
        if (fleeing.size() > MAX_TRACKED) {
            fleeing.clear();
        }
        if (lastHurt.size() > MAX_TRACKED) {
            lastHurt.clear();
        }
    }

    private List<VillagerEntity> collectVillagers(MinecraftClient client) {
        List<VillagerEntity> out = new ArrayList<>();
        if (client.player == null || client.world == null) {
            return out;
        }
        Vec3d eye = client.player.getEntityPos();
        Box box = new Box(eye, eye).expand(NEARBY_RADIUS);
        for (VillagerEntity villager : client.world.getEntitiesByClass(VillagerEntity.class, box, e -> true)) {
            if (villager.getEntityPos().squaredDistanceTo(eye) <= NEARBY_RADIUS_SQ) {
                out.add(villager);
            }
        }
        return out;
    }

    /**
     * The monster's name as the player's client spells it, lower case and without a leading article.
     *
     * <p>Minecraft already translates "Zombie", "Skeleton" or "Creeper" into the client language, so
     * the prompt gets a word the villagers would actually use instead of an entity id.
     */
    private static String mobName(HostileEntity hostile) {
        try {
            String name = hostile.getName().getString();
            if (name != null && !name.isBlank()) {
                return name.toLowerCase(java.util.Locale.ROOT);
            }
        } catch (Exception | Error e) {
            // A custom mob with a broken name should not break the flee reaction.
        }
        try {
            return hostile.getType().getName().getString().toLowerCase(java.util.Locale.ROOT);
        } catch (Exception | Error e) {
            return null;
        }
    }

    private void checkFleeing(MinecraftClient client, List<VillagerEntity> villagers, String lang, ModRuntime runtime) {
        if (client.world == null || client.player == null) {
            return;
        }
        List<HostileEntity> hostiles = new ArrayList<>();
        Vec3d eye = client.player.getEntityPos();
        Box box = new Box(eye, eye).expand(HOSTILE_RADIUS * 2);
        for (HostileEntity hostile : client.world.getEntitiesByClass(HostileEntity.class, box, e -> true)) {
            if (hostile.squaredDistanceTo(client.player) <= HOSTILE_RADIUS_SQ * 4) {
                hostiles.add(hostile);
            }
        }
        if (hostiles.isEmpty()) {
            fleeing.clear();
            return;
        }
        VillagerAiService service = runtime.service();
        for (VillagerEntity villager : villagers) {
            // The name of the closest monster that scares this villager, or null when it is calm.
            // Without a name the prompt says only "a monster", and the model
            // filled the gap with whatever it felt like - a villager standing next to a zombie kept
            // shouting about a skeleton. The name now comes from the entity itself, so the word in
            // the prompt matches the thing actually in the world.
            String threat = null;
            double closest = Double.MAX_VALUE;
            for (HostileEntity hostile : hostiles) {
                double distance = villager.squaredDistanceTo(hostile);
                if (distance <= HOSTILE_RADIUS_SQ && distance < closest) {
                    closest = distance;
                    threat = mobName(hostile);
                }
            }
            UUID id = villager.getUuid();
            boolean wasScared = fleeing.contains(id);
            if (threat != null && !wasScared) {
                fleeing.add(id);
                VillagerProfile profile = profileFor(villager, lang, runtime);
                WorldContext ctx = VillagerContextReader.read(villager);
                service.askReaction(id, "flee", threat, lang, profile, ctx,
                        text -> sayNow(villagerName(profile), text), errorHandler(client));
            } else if (threat == null && wasScared) {
                fleeing.remove(id);
            }
        }
        if (fleeing.size() > MAX_TRACKED) {
            fleeing.clear();
        }
    }

    private void maybeSpontaneous(MinecraftClient client, List<VillagerEntity> villagers, String lang,
                                  ModRuntime runtime, com.justaivillagers.core.ModConfig config) {
        // Let the world settle first.
        if (tickCounter < JOIN_GRACE_TICKS) {
            return;
        }
        long now = client.world == null ? 0 : client.world.getTime();
        // Only one villager may start talking at a time across the whole crowd.
        if (lastAnyTalk >= 0 && now - lastAnyTalk < MIN_TICKS_BETWEEN_ANY_TALK) {
            return;
        }
        VillagerAiService service = runtime.service();
        for (VillagerEntity villager : villagers) {
            if (Math.random() >= config.spontaneousChance * 20) {
                continue;
            }
            UUID id = villager.getUuid();
            Long last = lastSpoke.get(id);
            if (last != null && now - last < MIN_TICKS_BETWEEN_TALK) {
                continue;
            }
            lastSpoke.put(id, now);
            lastAnyTalk = now;
            if (lastSpoke.size() > MAX_TRACKED) {
                lastSpoke.clear();
            }
            VillagerProfile profile = profileFor(villager, lang, runtime);
            WorldContext ctx = VillagerContextReader.read(villager);
            service.askSpontaneous(id, lang, profile, ctx,
                    text -> say(client, villagerName(profile), text), errorHandler(client));
            // One line per check; the next villager gets a slot on a later tick.
            return;
        }
    }

    private void maybeGroupDialogue(MinecraftClient client, List<VillagerEntity> villagers, String lang,
                                    ModRuntime runtime) {
        if (villagers.size() < 2 || runtime.service().isGroupDialogueRunning()) {
            return;
        }
        VillagerProfile first = profileFor(villagers.get(0), lang, runtime);
        VillagerProfile second = profileFor(villagers.get(1), lang, runtime);
        WorldContext ctx = VillagerContextReader.read(villagers.get(0));
        VillagerEntity firstEntity = villagers.get(0);
        VillagerEntity secondEntity = villagers.get(1);
        runtime.service().runGroupDialogue(lang, first, second, ctx, 4,
                // The service already waits between its own lines, so this skips the queue's pause.
                line -> speech.enqueueImmediate(null, line),
                // Both speakers must still be in the world: the evening chat must not carry on after
                // one of them was killed, with the dead villager still taking turns.
                () -> alive(firstEntity) && alive(secondEntity));
    }

    /** Entry point from the attack hook. */
    public void onVillagerHurt(MinecraftClient client, VillagerEntity villager) {
        ModRuntime runtime = ModRuntime.get();
        if (!runtime.config().enableEventReactions) {
            return;
        }
        // Belt and braces: the side check in the callback already drops the server-side duplicate.
        // The cooldown is generous and wall-clock based: a player can land many hits per
        // second, and without it every punch would cost a full LLM request.
        long now = System.currentTimeMillis();
        UUID id = villager.getUuid();
        Long previous = lastHurt.get(id);
        if (previous != null && now - previous < runtime.config().hurtCooldownMs) {
            return;
        }
        lastHurt.put(id, now);
        if (lastHurt.size() > MAX_TRACKED) {
            lastHurt.clear();
        }
        // Not asking yet: the blow has not been applied, so a killing hit would still be answered by
        // a villager who is about to die. The next tick decides.
        pendingHurt.add(new PendingHurt(villager, GameBridge.language(runtime.config().languageOverride)));
    }

    /**
     * Turns hits seen last tick into reactions, but only for villagers that survived them.
     *
     * <p>This is the "died, then complained about being hit" bug: a fatal blow arrived, the villager
     * was already gone, and a moment later its reaction line appeared in chat.
     */
    private void resolvePendingHurts(MinecraftClient client) {
        if (pendingHurt.isEmpty()) {
            return;
        }
        ModRuntime runtime = ModRuntime.get();
        java.util.Iterator<PendingHurt> it = pendingHurt.iterator();
        while (it.hasNext()) {
            PendingHurt pending = it.next();
            it.remove();
            VillagerEntity villager = pending.villager;
            // isRemoved covers the despawn that follows death; getHealth covers the same tick, before
            // the entity is removed from the world.
            if (villager == null || villager.isRemoved() || !villager.isAlive() || villager.getHealth() <= 0.0f) {
                continue;
            }
            if (!runtime.config().enableEventReactions) {
                continue;
            }
            String lang = pending.lang;
            VillagerProfile profile = profileFor(villager, lang, runtime);
            WorldContext ctx = VillagerContextReader.read(villager);
            UUID id = villager.getUuid();
            runtime.service().askReaction(id, "hurt", null, lang, profile, ctx,
                    // The answer can take seconds, and the villager may be gone by then.
                    text -> {
                        if (!villager.isRemoved() && villager.isAlive() && villager.getHealth() > 0.0f) {
                            sayNow(villagerName(profile), text);
                        }
                    },
                    errorHandler(client));
        }
    }

    private VillagerProfile profileFor(VillagerEntity villager, String lang, ModRuntime runtime) {
        return runtime.profiles().get(villager.getUuid(), villager.isBaby(), lang);
    }

    private static String villagerName(VillagerProfile profile) {
        return profile == null ? "?" : profile.name;
    }

    /** Queues a line for chat; pacing happens in {@link SpeechQueue}, not here. */
    private void say(MinecraftClient client, String name, String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        speech.enqueue(name, text, ModRuntime.get().config());
    }

    /**
     * Shows a reaction immediately. A punch or a monster is an event, so it must not wait for the
     * speaking pace or for a queued conversation line to finish.
     */
    private void sayNow(String name, String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        speech.enqueueReaction(name, text);
    }

    /** Whether a villager is still standing in the world and not in the middle of dying. */
    private static boolean alive(VillagerEntity villager) {
        return villager != null && !villager.isRemoved() && villager.isAlive() && villager.getHealth() > 0.0f;
    }

    private static VillagerAiService.ErrorCallback errorHandler(MinecraftClient client) {
        return (kind, message) -> {
            // A rate-limit and a dead backend are both already explained elsewhere: the join hint
            // covers "Ollama is not running", and repeated identical lines would only flood chat
            // while every generation fails.
            if (kind == LlmException.Kind.OVERLOADED || kind == LlmException.Kind.UNAVAILABLE) {
                return;
            }
            client.execute(() -> {
                if (client.player != null) {
                    client.player.sendMessage(net.minecraft.text.Text.literal("§8[justaivillagers] " + message), false);
                }
            });
        };
    }

    private static boolean isNight(MinecraftClient client) {
        if (client.world == null) {
            return false;
        }
        long time = client.world.getTimeOfDay() % 24000L;
        return time >= 13000L && time < 23000L;
    }

    private static boolean isEvening(MinecraftClient client) {
        if (client.world == null) {
            return false;
        }
        long time = client.world.getTimeOfDay() % 24000L;
        return time >= 11000L && time < 12000L;
    }
}
