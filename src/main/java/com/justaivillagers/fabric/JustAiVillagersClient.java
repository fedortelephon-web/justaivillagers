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
            if (hint == null) {
                return;
            }
            ModRuntime.LOGGER.info("Backend hint: {}", hint);
            client.execute(() -> {
                pendingHint = hint + "\n" + com.justaivillagers.core.BackendHint.unavailableBanner(lang);
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
        for (String line : hint.split("\n")) {
            client.player.sendMessage(Text.literal("§8[justaivillagers] §c" + line), false);
        }
    }

    private void onEndTick(MinecraftClient client) {
        // Settings must be reachable without a world: a player whose Ollama is not set up yet needs
        // the settings screen precisely when villagers cannot talk, which includes the main menu.
        if (settingsKey != null && settingsKey.wasPressed() && client.currentScreen == null) {
            client.setScreen(new VillagerSettingsScreen(null));
            return;
        }
        if (client.player == null || client.world == null) {
            return;
        }
        flushHint(client);
        if (dialogKey.wasPressed()) {
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
