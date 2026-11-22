package com.justaivillagers.fabric;

import java.util.ArrayList;
import java.util.List;

import com.justaivillagers.core.VillagerProfile;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/** Client entry point: keybind, tick loop, world lifecycle, attack hook. */
public final class JustAiVillagersClient implements ClientModInitializer {

    public static final String MOD_ID = "justaivillagers";

    /** How far around the player villagers are picked up when talking to a group. */
    private static final double NEARBY_RADIUS = 12.0;
    /** Upper bound on recipients, so one keypress cannot spam every villager in a village. */
    private static final int MAX_NEARBY = 5;

    private KeyBinding dialogKey;
    private KeyBinding settingsKey;

    /** The live keybinds, so the settings screen can show and rebind them. */
    private static KeyBinding dialogBinding;
    private static KeyBinding settingsBinding;

    static KeyBinding dialogBinding() {
        return dialogBinding;
    }

    static KeyBinding settingsBinding() {
        return settingsBinding;
    }
    private final VillagerWatcher watcher = new VillagerWatcher();

    /**
     * Chat line waiting to be delivered, or {@code null}.
     *
     * <p>The health check on a local Ollama fails in a millisecond (connection refused), so the
     * result often arrives before the client has a player object. Sending straight from the checker
     * thread then silently lost the message. It is parked here and flushed on the first tick that
     * has a player instead.
     */
    private volatile String pendingHint;

    /** Minimum gap between repeated "backend is down" reminders, in milliseconds. */
    private static final long BACKEND_REMINDER_MS = 45_000L;

    /** When the last reminder was sent, so talking does not spam the chat. */
    private long lastBackendReminder;

    @Override
    public void onInitializeClient() {
        // 1.21.1 has no KeyBinding.Category type; the third argument is still the translation key.
        dialogKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.justaivillagers.dialog",
                GLFW.GLFW_KEY_V,
                "key.categories.justaivillagers.general"));
        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.justaivillagers.settings",
                GLFW.GLFW_KEY_O,
                "key.categories.justaivillagers.general"));
        dialogBinding = dialogKey;
        settingsBinding = settingsKey;

        // The core cannot write to chat, so it is handed a sink. Every debug line becomes one
        // prefixed chat message on the game thread.
        com.justaivillagers.core.DebugChat.setSink(text -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.execute(() -> {
                if (client.player != null) {
                    client.player.sendMessage(Text.literal("§8[justaivillagers] §7" + text), false);
                }
            });
        });
        ModCommands.register();
        VillagerHooks.bind(watcher);
        ClientTickEvents.END_CLIENT_TICK.register(this::onEndTick);

        // Exact hit detection instead of the original attack-cooldown heuristic.
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            // In single player the integrated server runs in this same JVM and fabric fires this
            // callback from both the client and the server attack path, so without the side check
            // one punch produced two identical reactions. The client copy is the one we want.
            if (world != null && !world.isClient()) {
                return net.minecraft.util.ActionResult.PASS;
            }
            if (entity instanceof VillagerEntity villager) {
                MinecraftClient client = MinecraftClient.getInstance();
                client.execute(() -> VillagerHooks.onVillagerHurt(client, villager));
            }
            return net.minecraft.util.ActionResult.PASS;
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ModRuntime runtime = ModRuntime.get();
            runtime.refreshIfChanged();
            runtime.onWorldLeft();
            watcher.reset();
            warnIfBackendDown(runtime, client);
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ModRuntime.get().onWorldLeft();
            watcher.reset();
        });
    }

    /**
     * Runs the backend check off the render thread on world join.
     *
     * <p>A health request has a multi-second timeout, so doing it inline would freeze the join. The
     * result is only ever a chat line, which is fine to deliver late.
     */
    private void warnIfBackendDown(ModRuntime runtime, MinecraftClient client) {
        com.justaivillagers.core.ModConfig config = runtime.config();
        String lang = GameBridge.language(config.languageOverride);
        ModRuntime.LOGGER.info("World join: checking backend at {} ({})", config.baseUrl, config.model);
        Thread checker = new Thread(() -> {
            String hint = com.justaivillagers.core.BackendHint.forHealth(
                    runtime.service().health(), lang, config.model, config.baseUrl);
            // Always leave a trace in the log, even when the backend is healthy: a bug report needs
            // to show which model and endpoint were actually in use. The chat line stays rare.
            runtime.service().logHealthOnce();
            client.execute(() -> {
                // The settings hint is unconditional: a player who has just installed the mod has no
                // way of guessing that O opens anything, and it is the one thing needed to fix a
                // silent backend. It is therefore said whether or not the server answered.
                client.player.sendMessage(ModCommands.joinLine(), false);
                if (hint == null) {
                    return;
                }
                ModRuntime.LOGGER.info("Backend hint: {}", hint);
                pendingHint = hint;
                flushHint(client);
            });
        }, "justaivillagers-health");
        checker.setDaemon(true);
        checker.start();
    }

    /** Delivers the parked backend hint once a player exists; safe to call every tick. */
    private void flushHint(MinecraftClient client) {
        String hint = pendingHint;
        if (hint == null || client.player == null) {
            return;
        }
        pendingHint = null;
        sendHintLine(client, hint, 0xFF5555);
    }

    /**
     * Sends one hint line, turning any http(s) URL in it into a link the player can click.
     *
     * <p>Plain text URLs are useless in chat: they cannot be clicked and are cut off by the chat
     * width. Every URL is split out into its own styled component with a click event, so the mod
     * page and the Ollama download can be copied with one click.
     */
    private static void sendHintLine(MinecraftClient client, String text, int colour) {
        if (client.player == null) {
            return;
        }
        MutableText line = Text.literal("§8[justaivillagers] ");
        line.append(linkify(text, colour));
        client.player.sendMessage(line, false);
    }

    /** Splits a sentence into plain runs and clickable URLs. */
    private static MutableText linkify(String text, int colour) {
        MutableText out = Text.literal("");
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("https?://[^\\s)]+")
                .matcher(text);
        int last = 0;
        while (m.find()) {
            if (m.start() > last) {
                out.append(Text.literal(text.substring(last, m.start())).styled(st -> st.withColor(colour)));
            }
            String url = m.group();
            out.append(Text.literal(url).styled(st -> st
                    .withColor(0x55AAFF)
                    .withUnderline(true)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, url))));
            last = m.end();
        }
        if (last < text.length()) {
            out.append(Text.literal(text.substring(last)).styled(st -> st.withColor(colour)));
        }
        return out;
    }

    /**
     * Reminds the player that the backend is down, at most once every so often.
     *
     * <p>The first hint is easy to miss: it arrives while the world is still loading and scrolls
     * away. Pressing the talk key is the moment the player expects speech, so that is when the
     * reminder is worth repeating - but not on every single press, or it becomes noise.
     */
    private void repeatBackendHint(MinecraftClient client) {
        long now = System.currentTimeMillis();
        if (now - lastBackendReminder < BACKEND_REMINDER_MS) {
            return;
        }
        com.justaivillagers.core.LlmBackend.Health health = ModRuntime.get().service().health();
        if (health != null && health.available) {
            return;
        }
        String lang = GameBridge.language(ModRuntime.get().config().languageOverride);
        lastBackendReminder = now;
        sendHintLine(client, com.justaivillagers.core.BackendHint.repeatHint(lang), 0xFFAA00);
    }

    private void onEndTick(MinecraftClient client) {
        // A command asked for the settings screen last tick; chat has closed by now.
        if (client.currentScreen == null && ModCommands.takeOpenSettingsRequest()) {
            client.setScreen(new VillagerSettingsScreen(null));
            return;
        }
        // The settings keybind works while no other screen is open. Minecraft only polls keybinds
        // from the in-game input handler, so this cannot fire from the title screen; the screen is
        // still reachable within seconds of joining a world.
        if (settingsKey != null && settingsKey.wasPressed() && client.currentScreen == null) {
            client.setScreen(new VillagerSettingsScreen(null));
            return;
        }
        if (client.player == null || client.world == null) {
            return;
        }
        flushHint(client);
        if (dialogKey.wasPressed()) {
            repeatBackendHint(client);
            openDialog(client);
            return;
        }
        watcher.tick(client);
    }

    private void openDialog(MinecraftClient client) {
        ModRuntime runtime = ModRuntime.get();
        String lang = GameBridge.language(runtime.config().languageOverride);
        var target = client.crosshairTarget;

        List<VillagerEntity> villagers = new ArrayList<>();
        if (target instanceof net.minecraft.util.hit.EntityHitResult entityHit
                && entityHit.getEntity() instanceof VillagerEntity villager) {
            villagers.add(villager);
        } else {
            // Looking at nothing: address everyone standing around, up to the configured limit, so
            // the player does not have to aim at one specific villager to talk to a group.
            villagers.addAll(nearestVillagers(client, MAX_NEARBY));
        }

        if (villagers.isEmpty()) {
            if (client.player != null) {
                client.player.sendMessage(Text.translatable("justaivillagers.hint.no_villagers"), false);
            }
            return;
        }

        List<VillagerProfile> profiles = new ArrayList<>();
        for (VillagerEntity villager : villagers) {
            profiles.add(runtime.profiles().get(villager.getUuid(), villager.isBaby(), lang));
        }
        client.setScreen(new VillagerDialogScreen(villagers, profiles));
    }

    /** Villagers within {@link #NEARBY_RADIUS} blocks of the player, closest first, capped. */
    private List<VillagerEntity> nearestVillagers(MinecraftClient client, int limit) {
        List<VillagerEntity> found = new ArrayList<>();
        if (client.player == null || client.world == null) {
            return found;
        }
        Vec3d eye = client.player.getPos();
        Box box = new Box(eye, eye).expand(NEARBY_RADIUS);
        for (VillagerEntity villager : client.world.getEntitiesByClass(VillagerEntity.class, box, e -> true)) {
            if (villager.getPos().squaredDistanceTo(eye) <= NEARBY_RADIUS * NEARBY_RADIUS) {
                found.add(villager);
            }
        }
        found.sort(java.util.Comparator.comparingDouble(v -> v.getPos().squaredDistanceTo(eye)));
        if (found.size() > limit) {
            return new ArrayList<>(found.subList(0, limit));
        }
        return found;
    }
}
