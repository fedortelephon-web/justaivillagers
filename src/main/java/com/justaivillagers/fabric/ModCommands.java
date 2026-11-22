package com.justaivillagers.fabric;

import com.justaivillagers.core.LangData;
import com.justaivillagers.core.LangPack;
import com.justaivillagers.core.LlmBackend;
import com.justaivillagers.core.Lang;
import com.justaivillagers.core.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * Client-side commands under {@code /justaivillagers}.
 *
 * <p>Client-only: the mod never sends anything to a server, so no command needs operator rights or
 * a server-side mod. The commands exist for diagnosis; when villagers stay silent, {@code status}
 * reports whether the server is up, whether the model is downloaded and what is downloading,
 * without opening the GUI.
 */
public final class ModCommands {

    public static final String ROOT = "justaivillagers";

    private ModCommands() {
    }

    private static LangPack pack() {
        ModRuntime runtime = ModRuntime.get();
        return LangData.of(Lang.normalise(GameBridge.language(runtime.config().languageOverride)));
    }

    private static String t(String key, Object... args) {
        return pack().t(key, args);
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal(ROOT)
                    .executes(ctx -> help(ctx.getSource()))
                    .then(ClientCommandManager.literal("help").executes(ctx -> help(ctx.getSource())))
                    .then(ClientCommandManager.literal("status").executes(ctx -> status(ctx.getSource())))
                    .then(ClientCommandManager.literal("settings").executes(ctx -> settings(ctx.getSource())))
                    .then(ClientCommandManager.literal("reload").executes(ctx -> reload(ctx.getSource()))));
        });
    }

    /** One grey prefix for every answer, so the lines read as one block in chat. */
    private static void line(FabricClientCommandSource source, String text) {
        source.sendFeedback(Text.literal("§8[justaivillagers] §r" + text));
    }

    private static void head(FabricClientCommandSource source, String text) {
        source.sendFeedback(Text.literal("§8[justaivillagers] §e" + text));
    }

    private static void ok(FabricClientCommandSource source, String text) {
        source.sendFeedback(Text.literal("§8[justaivillagers] §a" + text));
    }

    private static void bad(FabricClientCommandSource source, String text) {
        source.sendFeedback(Text.literal("§8[justaivillagers] §c" + text));
    }

    private static int help(FabricClientCommandSource source) {
        head(source, t("cmd.help.header"));
        line(source, t("cmd.help.status"));
        line(source, t("cmd.help.settings"));
        line(source, t("cmd.help.reload"));
        line(source, t("cmd.help.help"));
        return 1;
    }

    private static int status(FabricClientCommandSource source) {
        ModRuntime runtime = ModRuntime.get();
        ModConfig config = runtime.config();

        // A health probe is a network call, but a command is an explicit "tell me now", so the
        // cached answer is exactly the wrong one here: it would report the state from a minute ago.
        LlmBackend.Health health = runtime.healthNow();

        head(source, t("cmd.status.header"));
        line(source, t("cmd.status.address", config.baseUrl));

        boolean installed = health != null && health.hasModel(config.model);
        String modelLine = t("cmd.status.model", config.model);
        if (health != null && !health.available && !installed) {
            modelLine = t("cmd.status.model.missing", config.model);
        }
        line(source, modelLine);

        if (health != null && health.available) {
            ok(source, t("cmd.status.state.ok"));
        } else {
            bad(source, t("cmd.status.state.down"));
        }

        String pull = VillagerSettingsScreen.pullSummary();
        if (pull != null) {
            line(source, pull);
        }

        line(source, t("cmd.status.hint", keyName()));
        return 1;
    }

    private static int settings(FabricClientCommandSource source) {
        // Opening the screen straight from the command does nothing visible: the chat screen that
        // sent the command closes itself right after dispatch and takes the new screen with it. The
        // request is parked and opened on the next client tick, when chat is properly gone.
        openSettingsNextTick = true;
        return 1;
    }

    /** Set by {@code /justaivillagers settings}; consumed by the client tick loop. */
    private static volatile boolean openSettingsNextTick;

    /** True once, if a command asked for the settings screen; the caller opens and clears it. */
    static boolean takeOpenSettingsRequest() {
        if (!openSettingsNextTick) {
            return false;
        }
        openSettingsNextTick = false;
        return true;
    }

    private static int reload(FabricClientCommandSource source) {
        ModRuntime runtime = ModRuntime.get();
        // refreshIfChanged only reacts to a file that changed while the game was running, which is
        // the common case for someone editing the JSON by hand. When the file is untouched it does
        // nothing, so say so rather than pretending something was reloaded.
        long before = runtime.configStamp();
        runtime.refreshIfChanged();
        if (runtime.configStamp() == before) {
            line(source, t("cmd.reload.same", runtime.configFile().toString()));
        } else {
            ok(source, t("cmd.reload.done"));
        }
        return 1;
    }

    /** The settings key as the player's keyboard spells it, or "O" if that cannot be asked yet. */
    public static String keyName() {
        try {
            net.minecraft.client.option.KeyBinding binding = JustAiVillagersClient.settingsBinding();
            if (binding == null) {
                return "O";
            }
            return binding.getBoundKeyLocalizedText().getString();
        } catch (Exception | Error e) {
            return "O";
        }
    }

    /** Plain text with no chat colours, for the world-join line. */
    static MutableText joinLine() {
        return Text.literal("§8[justaivillagers] §a" + t("join.hint", keyName()));
    }
}
