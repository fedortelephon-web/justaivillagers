package com.justaivillagers.fabric;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.justaivillagers.core.Lang;
import com.justaivillagers.core.LangData;
import com.justaivillagers.core.LangPack;
import com.justaivillagers.core.LlmBackend;
import com.justaivillagers.core.ModelCatalog;
import com.justaivillagers.core.ModelPuller;
import com.justaivillagers.core.ModConfig;
import com.justaivillagers.core.OllamaInstaller;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * The mod's settings screen, opened with a keybind.
 *
 * <p>Everything the JSON file can express is reachable here, because a player who has to edit JSON
 * to change the model is a player who will not change the model. Settings are edited on a copy and
 * only written on Save, so Cancel really cancels.
 *
 * <p>Layout is computed from the space actually available rather than from constants. Minecraft's
 * GUI scale can leave as little as 240 units of height, and a fixed 22-pixel row height overflowed
 * onto the Save button there; rows therefore shrink to fit instead of running off the screen.
 */
public final class VillagerSettingsScreen extends Screen {

    private static final int TAB_MODEL = 0;
    private static final int TAB_BEHAVIOUR = 1;
    private static final int TAB_LANGUAGE = 2;

    /** Bottom bar height, reserved before the content area is measured. */
    private static final int BOTTOM_BAR = 26;

    /** Tab row offset; the title only gets its own line when the window is tall enough. */
    private int tabY() {
        return this.height >= 300 ? 16 : 6;
    }

    private int topBar() {
        return tabY() + 24;
    }

    /** Ollama's language list, in the order the language tab lists them. */
    private static final String[] LANG_CHOICES = {
            "", Lang.EN, Lang.RU, Lang.ES, Lang.PT, Lang.DE, Lang.PL, Lang.FR, Lang.IT, Lang.TR,
    };

    private final Screen parent;
    private final ModConfig draft;
    private int tab = TAB_MODEL;

    /** Content geometry, filled in by build() so render() does not have to recompute it. */
    private int contentX;
    private int contentWidth;
    private int rowHeight = 20;
    private int progressY;

    private TextFieldWidget urlField;
    private TextFieldWidget customModelField;

    /** Which keybind row is waiting for a key press: 0 none, 1 dialog, 2 settings. */
    private int capturingKey;


    /** Set by a listener when the widget state must change; tick() performs the rebuild. */
    private volatile boolean needsRebuild;

    /**
     * A model download that outlives this screen object.
     *
     * <p>Closing the screen is not the same as cancelling the download: the pull runs on a daemon
     * thread owned by Ollama, and rebuilding the screen from scratch would lose the bar. The state
     * therefore lives in a static holder that the next {@code VillagerSettingsScreen} picks up, so
     * pressing O again shows the same progress rather than a fresh, empty tab.
     */
    private static final class PullState {
        private static ModelPuller puller;
        private static String model;
        private static long done;
        private static long total;
        private static String status = "";
        private static boolean finished;
        private static String outcome = "";
        private static boolean outcomeError;
    }

    /** Installer download in flight, if any. */
    private OllamaInstaller installer;
    private long installerDone;
    private long installerTotal;
    private Path installerSaved;
    private String installerError = "";

    private LlmBackend.Health health;
    private String statusLine = "";

    /** Model name that was actually probed, so the label cannot claim an untested one. */
    private String probedModel = "";

    /** Line under the Ollama buttons that says where the copied command goes. */
    private int hintY = -1;

    /** Bounds of the help button, so render() can frame it in yellow. */
    private int helpX = -1;
    private int helpY = -1;
    private int helpW;
    private int helpH;

    public VillagerSettingsScreen(Screen parent) {
        super(Text.literal("Just AI Villagers"));
        this.parent = parent;
        // Edit a copy: the live config must not change until Save is pressed.
        this.draft = ModRuntime.get().config().copy();
    }

    private static LangPack pack() {
        ModRuntime runtime = ModRuntime.get();
        return LangData.of(Lang.normalise(GameBridge.language(runtime.config().languageOverride)));
    }

    private static String t(String key, Object... args) {
        return pack().t(key, args);
    }

    @Override
    protected void init() {
        // The health probe is a network call, but this runs on the game thread only once per screen
        // open with a short timeout; the backend caches it so later opens are instant. A forced
        // probe is what makes a model that finished downloading show up as installed on reopen.
        health = ModRuntime.get().healthNow();
        probedModel = ModRuntime.get().config().model;
        build();
    }

    /** Wipes and rebuilds every widget; called when the tab or a value changes. */
    private void rebuild() {
        // Text the player already typed must survive a rebuild, which happens on every slider drag.
        String urlText = urlField != null ? urlField.getText() : null;
        String customText = customModelField != null ? customModelField.getText() : null;
        clearChildren();
        build();
        if (urlField != null && urlText != null) {
            urlField.setText(urlText);
        }
        if (customModelField != null && customText != null) {
            customModelField.setText(customText);
        }
    }

    private void build() {
        int cx = this.width / 2;

        // Tabs across the top.
        int tabWidth = Math.min(110, (this.width - 20) / 3);
        String[] labels = { t("settings.tab.model"), t("settings.tab.features"), t("settings.tab.language") };
        for (int i = 0; i < 3; i++) {
            final int index = i;
            int x = cx - (tabWidth * 3 + 8) / 2 + i * (tabWidth + 4);
            ButtonWidget button = ButtonWidget.builder(Text.literal(labels[i]), b -> {
                tab = index;
                rebuild();
            }).dimensions(x, tabY(), tabWidth, 18).build();
            // The active tab is not clickable, which makes it read as "you are here".
            button.active = tab != index;
            addDrawableChild(button);
        }

        int rows = rowCount();
        int available = this.height - topBar() - BOTTOM_BAR;
        // Rows shrink rather than overflow; 13 is still tall enough to read 8-pixel text.
        rowHeight = Math.max(13, Math.min(20, available / Math.max(1, rows)));

        this.contentWidth = Math.min(340, this.width - 24);
        this.contentX = cx - contentWidth / 2;

        if (tab == TAB_MODEL) {
            buildModelTab();
        } else if (tab == TAB_BEHAVIOUR) {
            buildBehaviourTab();
        } else {
            buildLanguageTab();
        }

        // Save / Cancel pinned to the bottom, never overlapping the content area. Two buttons only:
        // Cancel and Close both mean "leave without saving", so having both was one more choice to
        // read for no gain, and Cancel on the left matches the order of the other screens.
        int buttonY = this.height - BOTTOM_BAR + 3;
        int bw = Math.min(120, (this.width - 20) / 2);
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.cancel")), b -> close())
                .dimensions(cx - bw - 4, buttonY, bw, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.save")), b -> save())
                .dimensions(cx + 4, buttonY, bw, 20).build());
    }

    /** How many rows the active tab needs; used only to size rows before laying them out. */
    private int rowCount() {
        if (tab == TAB_MODEL) {
            // Address, check, guide, five catalogue models, custom id, Ollama helper, hint lines.
            return 5 + ModelCatalog.ENTRIES.size() + 3;
        }
        if (tab == TAB_BEHAVIOUR) {
            return 5 + 4 + 2;
        }
        return 1 + LANG_CHOICES.length;
    }

    private int contentTop() {
        return topBar();
    }

    // ---------------------------------------------------------------- model tab

    private void buildModelTab() {
        int x = contentX;
        int w = contentWidth;
        int y = contentTop();
        int fieldH = Math.max(12, rowHeight - 2);
        this.hintY = -1;
        this.helpX = -1;

        // Server address. The field is a couple of pixels narrower than the buttons because a text
        // field draws a border inside its box, so at equal widths it looked wider than the rows.
        this.urlField = new TextFieldWidget(this.textRenderer, x + 2, y, w - 4, fieldH,
                Text.literal(t("settings.server")));
        this.urlField.setMaxLength(200);
        this.urlField.setPlaceholder(Text.literal(t("settings.server.hint")));
        this.urlField.setText(draft.baseUrl);
        this.urlField.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                Text.literal(t("settings.server.tip"))));
        addDrawableChild(this.urlField);
        y += rowHeight;

        // Health line and the recheck button share a row.
        int checkW = Math.min(96, w / 3);
        ButtonWidget check = ButtonWidget.builder(Text.literal(t("settings.check")), b -> {
            probeDraft();
            rebuild();
        }).dimensions(x, y, checkW, fieldH).build();
        check.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                Text.literal(t("settings.check.tip"))));
        addDrawableChild(check);
        y += rowHeight;

        // One row per catalogue entry: select on the left, download on the right.
        int downloadW = Math.max(40, Math.min(78, w / 4));
        for (ModelCatalog.Entry entry : ModelCatalog.ENTRIES) {
            final ModelCatalog.Entry target = entry;
            String name = entry.label();
            String detail = " " + ModelCatalog.sizeLabel(entry) + " / " + ModelCatalog.ramLabel(entry);
            String label = (entry.id.equals(draft.model) ? "> " : "") + name + detail;
            label = this.textRenderer.trimToWidth(label, w - downloadW - 12);
            ButtonWidget select = ButtonWidget.builder(Text.literal(label), b -> {
                draft.model = target.id;
                // rebuild() restores the text the player had typed, on purpose, so that a slider drag
                // cannot wipe an address. That also means selecting a model leaves the old name
                // in the field; writing it here makes the picker do what it looks like it does.
                if (customModelField != null) {
                    customModelField.setText(target.id);
                }
                rebuild();
            }).dimensions(x, y, w - downloadW - 4, fieldH).build();
            select.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(Text.literal(modelTip(entry))));
            addDrawableChild(select);

            boolean isPulling = PullState.model != null && PullState.model.equals(entry.id);
            // A downloaded model says so and cannot be downloaded again: the button would only
            // re-fetch the same bytes, and a greyed-out row is the clearest way to show "you have
            // this one already". In-flight pulls keep their own state.
            boolean installed = !isPulling && health != null && health.hasModel(entry.id);
            String downloadText = isPulling
                    ? "..."
                    : this.textRenderer.trimToWidth(
                            t(installed ? "settings.model.installed" : "settings.model.get"),
                            downloadW - 8);
            ButtonWidget download = ButtonWidget.builder(
                    Text.literal(downloadText),
                    b -> startPull(target.id)).dimensions(x + w - downloadW, y, downloadW, fieldH).build();
            download.active = !installed && PullState.model == null;
            download.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(Text.literal(
                    installed ? t("settings.model.installed.tip")
                            : t("settings.model.get.tip", entry.id))));
            addDrawableChild(download);
            y += rowHeight;
        }

        // Name of the selected model, editable for anything not in the catalogue.
        this.customModelField = new TextFieldWidget(this.textRenderer, x + 2, y, w - 4, fieldH,
                Text.literal(t("settings.model.custom")));
        this.customModelField.setMaxLength(120);
        this.customModelField.setPlaceholder(Text.literal(t("settings.model.custom.hint")));
        this.customModelField.setText(draft.model);
        this.customModelField.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                Text.literal(t("settings.model.custom.tip"))));
        addDrawableChild(this.customModelField);
        y += rowHeight;

        // The guide button. Always visible, not only when the backend is down: a player may want to
        // read ahead before downloading anything, and a button that appears and disappears is hard
        // to find twice.
        {
            ButtonWidget help = ButtonWidget.builder(
                    Text.literal(t("settings.help")).styled(st -> st.withColor(0xFFD54F).withBold(true)),
                    b -> openHelp()).dimensions(x, y, w, fieldH).build();
            help.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                    Text.literal(t("settings.help.tip"))));
            this.helpX = x;
            this.helpY = y;
            this.helpW = w;
            this.helpH = fieldH;
            addDrawableChild(help);
            y += rowHeight;
        }

        // Cancelling is available from the first moment of the download, on its own full-width row
        // so it is impossible to miss.
        if (installer != null) {
            ButtonWidget cancel = ButtonWidget.builder(
                    Text.literal(t("settings.ollama.cancel")), b -> cancelInstaller())
                    .dimensions(x, y, w, fieldH).build();
            cancel.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                    Text.literal(t("settings.ollama.cancel.tip"))));
            addDrawableChild(cancel);
            y += rowHeight;
        }

        // Everything below this point is painted text and bars, never widgets, so it starts under
        // the last button instead of sharing its line.
        this.progressY = y;
        if (this.hintY < 0) {
            this.hintY = y;
        }
    }

    /** A one-line description of a catalogue model, for the row's tooltip. */
    private String modelTip(ModelCatalog.Entry entry) {
        String base = t("settings.model.tip", entry.label(), ModelCatalog.sizeLabel(entry),
                ModelCatalog.ramLabel(entry));
        String extra = t("settings.model.tip." + entry.id.replace(':', '_').replace('.', '_').replace('-', '_'));
        // A missing translation comes back as its own key; only append a real sentence.
        if (extra == null || extra.isBlank() || extra.startsWith("settings.model.tip.")) {
            return base;
        }
        return base + "\n" + extra;
    }

    private void startPull(String model) {
        if (PullState.model != null) {
            return;
        }
        PullState.model = model;
        PullState.finished = false;
        PullState.done = 0;
        PullState.total = -1;
        PullState.status = "";
        PullState.outcome = "";
        PullState.outcomeError = false;

        // Pull against the address currently in the field, so a player who just fixed the URL does
        // not have to save first to make the download work.
        ModConfig probe = draft.copy();
        probe.baseUrl = urlField != null ? urlField.getText() : draft.baseUrl;

        final ModelPuller active = new ModelPuller(probe, ModRuntime.LOGGER);
        PullState.puller = active;
        active.pull(model, new ModelPuller.Listener() {
            @Override
            public void onStatus(String text) {
                PullState.status = text;
            }

            @Override
            public void onProgress(long completed, long total) {
                PullState.done = completed;
                PullState.total = total;
            }

            @Override
            public void onDone() {
                PullState.finished = true;
                needsRebuild = true;
                PullState.model = null;
                PullState.outcome = t("settings.model.done", model);
                PullState.outcomeError = false;
                // The download outlives the screen, so chat is the only place its result is still
                // visible once the player has walked away from the menu.
                announce(PullState.outcome, true);
                draft.model = model;
                // Download buttons depend on what the server now has, so refresh the probe.
                probeDraft();
            }

            @Override
            public void onError(String message) {
                PullState.status = message;
                needsRebuild = true;
                PullState.model = null;
                PullState.outcome = t("settings.model.failed", model, message);
                PullState.outcomeError = true;
                announce(PullState.outcome, false);
            }
        });
        rebuild();
    }

    /**
     * The current model download as one chat line, or {@code null} when nothing is downloading.
     *
     * <p>Used by {@code /justaivillagers status}, which is where a player looks when the screen is
     * closed and the progress bar is therefore invisible.
     */
    static String pullSummary() {
        if (PullState.model == null) {
            return null;
        }
        if (PullState.total > 0) {
            int percent = (int) Math.min(100L, PullState.done * 100L / PullState.total);
            return t("settings.model.downloading", percent + "%") + " " + PullState.model;
        }
        return t("settings.model.downloading", PullState.status.isEmpty() ? "..." : PullState.status)
                + " " + PullState.model;
    }

    /** Posts a one-line result to chat, if there is a player to read it. */
    private static void announce(String text, boolean ok) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || text == null || text.isEmpty()) {
            return;
        }
        client.player.sendMessage(
                Text.literal("§8[justaivillagers] " + (ok ? "§a" : "§c") + text), false);
    }

    private void startInstaller() {
        if (installer != null) {
            return;
        }
        installerSaved = null;
        installerError = "";
        installerDone = 0;
        installerTotal = -1;
        Path dir = ModRuntime.get().configFile().getParent();
        OllamaInstaller active = new OllamaInstaller(ModRuntime.LOGGER);
        this.installer = active;
        // Without this the Cancel button only appeared on the next unrelated interaction, because
        // the widget list is rebuilt on a flag rather than every frame.
        needsRebuild = true;
        active.download(dir, new OllamaInstaller.Listener() {
            @Override
            public void onProgress(long completed, long total) {
                installerDone = completed;
                installerTotal = total;
            }

            @Override
            public void onDone(Path saved) {
                installerSaved = saved;
                needsRebuild = true;
                installer = null;
            }

            @Override
            public void onError(String message) {
                installerError = message;
                needsRebuild = true;
                installer = null;
            }
        });
    }


    /** Opens the setup guide, so the player can go back to the settings afterwards. */
    private void openHelp() {
        net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
        if (client != null) {
            client.setScreen(new HelpScreen(this));
        }
    }

    /**
     * Puts a string on the system clipboard and tells the player in chat.
     *
     * <p>Chat is used rather than a toast: a player who has just copied something needs to know the
     * copy happened, and the line also survives long enough to read.
     */
    private void copyToClipboard(String value, String confirmation) {
        net.minecraft.client.MinecraftClient.getInstance().keyboard.setClipboard(value);
        if (net.minecraft.client.MinecraftClient.getInstance().player != null) {
            net.minecraft.client.MinecraftClient.getInstance().player.sendMessage(
                    net.minecraft.text.Text.literal("§8[justaivillagers] §a" + confirmation), false);
        }
    }

    /** Stops an installer download that is still running. */
    private void cancelInstaller() {
        if (installer != null) {
            installer.cancel();
            installer = null;
            installerError = "";
            installerSaved = null;
            needsRebuild = true;
        }
    }

    /**
     * Probes the address and model currently on screen, not the saved ones.
     *
     * <p>This is the whole point of the button: a player who has just pasted a different address or
     * clicked another model expects Check to test that, and it looks broken when it silently tests
     * yesterday's settings instead.
     */
    private void probeDraft() {
        ModConfig probe = draft.copy();
        if (urlField != null && !urlField.getText().isBlank()) {
            probe.baseUrl = urlField.getText().trim();
        }
        if (customModelField != null && !customModelField.getText().isBlank()) {
            probe.model = customModelField.getText().trim();
        }
        probe.sanitize();
        this.probedModel = probe.model;
        try {
            if ("openai".equals(probe.provider)) {
                this.health = new com.justaivillagers.core.OpenAiBackend(probe, ModRuntime.LOGGER).health();
            } else {
                this.health = new com.justaivillagers.core.OllamaBackend(probe, ModRuntime.LOGGER).health(true);
            }
        } catch (Exception | Error e) {
            this.health = com.justaivillagers.core.LlmBackend.Health.down(e.toString());
            ModRuntime.LOGGER.warn("Health probe failed: {}", e.toString());
        }
        this.statusLine = healthLabel();
    }

    private String healthLabel() {
        if (health == null) {
            return t("settings.status.down");
        }
        if (health.available) {
            // Name the model that was probed, never one that merely happens to be selected.
            String model = probedModel.isEmpty() ? draft.model : probedModel;
            return t("settings.status.ok", model);
        }
        String detail = health.detail == null ? "" : health.detail;
        if (detail.contains("not installed")) {
            String model = probedModel.isEmpty() ? draft.model : probedModel;
            return t("settings.status.nomodel", model);
        }
        return t("settings.status.down");
    }

    // ------------------------------------------------------------ behaviour tab

    private void buildBehaviourTab() {
        int x = contentX;
        int w = contentWidth;
        int y = contentTop();
        int fieldH = Math.max(12, rowHeight - 2);

        y = toggle(x, y, w, fieldH, "settings.spontaneous", () -> draft.enableSpontaneousTalk,
                v -> draft.enableSpontaneousTalk = v);
        y = toggle(x, y, w, fieldH, "settings.group", () -> draft.enableGroupDialogues,
                v -> draft.enableGroupDialogues = v);
        y = toggle(x, y, w, fieldH, "settings.reactions", () -> draft.enableEventReactions,
                v -> draft.enableEventReactions = v);
        y = toggle(x, y, w, fieldH, "settings.fallback", () -> draft.enableFallbackLines,
                v -> draft.enableFallbackLines = v);
        y = toggle(x, y, w, fieldH, "settings.debug", () -> draft.debugPrompts,
                v -> draft.debugPrompts = v);

        y = slider(x, y, w, fieldH, "settings.temperature", () -> draft.temperature,
                v -> draft.temperature = v, 0.0, 2.0, 1);
        y = slider(x, y, w, fieldH, "settings.maxTokens", () -> (double) draft.maxTokens,
                v -> draft.maxTokens = (int) Math.round(v), 16, 512, 0);
        y = slider(x, y, w, fieldH, "settings.speechPace", () -> (double) draft.speechPaceMsPerChar,
                v -> draft.speechPaceMsPerChar = (int) Math.round(v), 0, 300, 0);
        y = slider(x, y, w, fieldH, "settings.history", () -> (double) draft.maxHistoryMessages,
                v -> draft.maxHistoryMessages = (int) Math.round(v), 2, 40, 0);

        // Keybinds last: they are set once and then forgotten, unlike the sliders above.
        y = keyRow(x, y, w, fieldH, "settings.key.dialog", 1, () -> JustAiVillagersClient.dialogBinding());
        keyRow(x, y, w, fieldH, "settings.key.settings", 2, () -> JustAiVillagersClient.settingsBinding());
    }

    private int toggle(int x, int y, int w, int h, String key, Supplier<Boolean> get,
            Consumer<Boolean> set) {
        String mark = get.get() ? "[x] " : "[ ] ";
        ButtonWidget button = ButtonWidget.builder(Text.literal(mark + t(key)), b -> {
            set.accept(!get.get());
            rebuild();
        }).dimensions(x, y, w, h).build();
        button.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                Text.literal(t(key + ".tip"))));
        addDrawableChild(button);
        return y + rowHeight;
    }

    /** A real slider: dragging is one gesture instead of dozens of clicks on a plus button. */
    private int slider(int x, int y, int w, int h, String key, Supplier<Double> get,
            Consumer<Double> set, double min, double max, int decimals) {
        final String label = t(key);
        ValueSlider widget = new ValueSlider(x, y, w, h, label, get.get(), min, max, decimals, set,
                () -> {
                    // Rebuild so the widget picks up the new value from the supplier.
                    rebuild();
                });
        widget.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                Text.literal(t(key + ".tip"))));
        addDrawableChild(widget);
        return y + rowHeight;
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    /** One row that shows a keybind and waits for a key press when clicked. */
    private int keyRow(int x, int y, int w, int h, String key, int which, Supplier<KeyBinding> binding) {
        KeyBinding bound = binding.get();
        String name = bound == null ? "?" : keyName(bound);
        boolean waiting = capturingKey == which;
        String text = t(key) + ": " + (waiting ? t("settings.key.press") : name);
        ButtonWidget button = ButtonWidget.builder(Text.literal(text), b -> {
            capturingKey = which;
            rebuild();
        }).dimensions(x, y, w, h).build();
        button.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                Text.literal(t(key + ".tip"))));
        addDrawableChild(button);
        return y + rowHeight;
    }

    @SuppressWarnings("deprecation")
    private static String keyName(KeyBinding binding) {
        try {
            return binding.getBoundKeyLocalizedText().getString();
        } catch (Exception | Error e) {
            return "?";
        }
    }

    // ------------------------------------------------------------- language tab

    private void buildLanguageTab() {
        int x = contentX;
        int w = contentWidth;
        int y = contentTop();
        int fieldH = Math.max(12, rowHeight - 2);

        addDrawableChild(ButtonWidget.builder(
                Text.literal(t("settings.language") + ": " + langLabel()), b -> {
                    draft.languageOverride = nextLanguage(draft.languageOverride, 1);
                    rebuild();
                }).dimensions(x, y, w, fieldH).build());
        y += rowHeight;

        // Every supported language as its own row: cycling ten languages one click at a time is
        // worse than seeing them all.
        for (String code : LANG_CHOICES) {
            String label = code.isEmpty() ? t("settings.language.auto") : nameOf(code);
            addDrawableChild(ButtonWidget.builder(
                    Text.literal((draft.languageOverride.equals(code) ? "> " : "") + label), b -> {
                        draft.languageOverride = code;
                        rebuild();
                    }).dimensions(x, y, w, fieldH).build());
            y += rowHeight;
        }
    }

    private String langLabel() {
        return draft.languageOverride.isEmpty()
                ? t("settings.language.auto")
                : nameOf(draft.languageOverride);
    }

    private static String nextLanguage(String current, int delta) {
        int index = 0;
        for (int i = 0; i < LANG_CHOICES.length; i++) {
            if (LANG_CHOICES[i].equals(current)) {
                index = i;
                break;
            }
        }
        return LANG_CHOICES[(index + delta + LANG_CHOICES.length) % LANG_CHOICES.length];
    }

    private static String nameOf(String code) {
        switch (code) {
            case Lang.EN: return "English";
            case Lang.RU: return "Русский";
            case Lang.ES: return "Español";
            case Lang.PT: return "Português";
            case Lang.DE: return "Deutsch";
            case Lang.PL: return "Polski";
            case Lang.FR: return "Français";
            case Lang.IT: return "Italiano";
            case Lang.TR: return "Türkçe";
            default: return code;
        }
    }

    // ------------------------------------------------------------------ actions

    @Override
    public boolean keyPressed(net.minecraft.client.input.KeyInput input) {
        if (capturingKey != 0) {
            int keyCode = input.key();
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                capturingKey = 0;
                rebuild();
                return true;
            }
            applyKey(capturingKey, keyCode);
            capturingKey = 0;
            rebuild();
            return true;
        }
        return super.keyPressed(input);
    }

    /**
     * Rebinds one key and writes options.txt.
     *
     * <p>Minecraft persists keybinds itself, so the mod does not keep its own copy; writing the
     * options file is what makes the change survive a restart.
     */
    private void applyKey(int which, int keyCode) {
        KeyBinding binding = which == 1
                ? JustAiVillagersClient.dialogBinding()
                : JustAiVillagersClient.settingsBinding();
        if (binding == null) {
            return;
        }
        try {
            binding.setBoundKey(InputUtil.Type.KEYSYM.createFromCode(keyCode));
            KeyBinding.updateKeysByCode();
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.options != null) {
                client.options.write();
            }
        } catch (Exception | Error e) {
            ModRuntime.LOGGER.warn("Could not rebind key {}: {}", which, e.toString());
        }
    }

    private void save() {
        ModConfig result = draft.copy();
        if (urlField != null && !urlField.getText().isBlank()) {
            result.baseUrl = urlField.getText().trim();
        }
        if (customModelField != null && !customModelField.getText().isBlank()) {
            result.model = customModelField.getText().trim();
        }
        ModRuntime.get().apply(result);
        ModRuntime.get().recheckBackend();
        close();
    }

    @Override
    public void close() {
        // A model download keeps running when the screen closes. Cancelling it here meant that
        // pressing O again showed an idle tab and an empty progress bar while Ollama was still
        // fetching gigabytes in the background, and the player had no way to see it finish.
        if (installer != null) {
            installer.cancel();
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.setScreen(parent);
        }
    }

    @Override
    public void tick() {
        // Downloads report progress off-thread; rebuilding the widgets every tick would destroy the
        // text fields under the player's cursor, so only state *transitions* rebuild the screen.
        if (needsRebuild) {
            needsRebuild = false;
            rebuild();
        }
    }

    // ----------------------------------------------------------------- render

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        // On a short window the tabs already name the screen; drawing the title above them would
        // overlap, so it is only shown when a spare line exists.
        if (this.height >= 300) {
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 2, 0xFFFFFF);
        }

        int x = contentX;
        int w = contentWidth;

        if (tab == TAB_MODEL) {
            // A yellow frame around the guide button. Text colour alone is easy to miss among the
            // other rows; a border reads as "this is the one to click".
            if (helpX >= 0) {
                context.fill(helpX - 1, helpY - 1, helpX + helpW + 1, helpY, 0xFFFFC107);
                context.fill(helpX - 1, helpY + helpH, helpX + helpW + 1, helpY + helpH + 1, 0xFFFFC107);
                context.fill(helpX - 1, helpY, helpX, helpY + helpH, 0xFFFFC107);
                context.fill(helpX + helpW, helpY, helpX + helpW + 1, helpY + helpH, 0xFFFFC107);
            }
            // The health line sits on the check row, to the right of the button.
            int checkW = Math.min(96, w / 3);
            boolean ok = health != null && health.available;
            context.drawTextWithShadow(this.textRenderer, Text.literal(trimStatus(healthLabel())),
                    x + checkW + 6, contentTop() + rowHeight + 4, ok ? 0x55FF55 : 0xFF5555);

            int lineY = progressY;
            int limit = this.height - BOTTOM_BAR;

            // Model download: one caption, one bar, then four pixels of breathing room.
            if (PullState.model != null) {
                boolean known = PullState.total > 0;
                float progress = known ? (float) PullState.done / (float) PullState.total : -1f;
                // A model can be several blobs, so the percentage legitimately restarts; the status
                // text says which phase is running so the bar does not look stuck or broken. Some
                // phases report no total at all, hence the sweep and the dots.
                String text = known
                        ? t("settings.model.downloading", percent(progress))
                        : t("settings.model.downloading", animatedDots());
                if (!PullState.status.isEmpty()) {
                    text = text + "  " + PullState.status;
                }
                drawLine(context, text, x, lineY, w, 0xFFFFFF, limit);
                drawBar(context, x, lineY + 10, w, progress);
                lineY += 18;
            } else if (!PullState.outcome.isEmpty()) {
                // A result stays on screen after the download ends: it is the only confirmation left
                // when the chat line has already scrolled away.
                drawLine(context, PullState.outcome, x, lineY, w,
                        PullState.outcomeError ? 0xFF5555 : 0x55FF55, limit);
                lineY += 12;
            } else if (!PullState.status.isEmpty()) {
                // A phase message with no verdict yet: the dots say the step is still moving rather
                // than showing a sentence that never changes.
                drawLine(context, PullState.status + animatedDots(), x, lineY, w, 0xFFAA00, limit);
                lineY += 12;
            }

            // Ollama installer: same treatment, on its own lines.
            if (installer != null) {
                boolean known = installerTotal > 0;
                float progress = known ? (float) installerDone / (float) installerTotal : -1f;
                drawLine(context, t("settings.model.downloading",
                                known ? percent(progress) : animatedDots()),
                        x, lineY, w, 0xFFFFFF, limit);
                drawBar(context, x, lineY + 10, w, progress);
            } else if (installerSaved != null) {
                drawLine(context, t("settings.ollama.saved", installerSaved.getFileName().toString()),
                        x, lineY, w, 0x55FF55, limit);
            } else if (!installerError.isEmpty()) {
                drawLine(context, installerError, x, lineY, w, 0xFF5555, limit);
            }
        }
    }

    /** Draws one line of status text, trimmed to the content width and never off the bottom. */
    /** Draws text cut into lines by width, so a long sentence does not run off the screen. */
    private void drawWrapped(DrawContext context, String text, int x, int y, int w, int colour, int limit) {
        java.util.List<net.minecraft.text.OrderedText> lines = this.textRenderer.wrapLines(
                net.minecraft.text.Text.literal(text), w);
        int row = y;
        for (net.minecraft.text.OrderedText line : lines) {
            if (row + 9 > limit) {
                return;
            }
            context.drawTextWithShadow(this.textRenderer, line, x, row, colour);
            row += 10;
        }
    }

    private void drawLine(DrawContext context, String text, int x, int y, int w, int colour, int limit) {
        if (y + 9 > limit) {
            return;
        }
        // Buttons draw their border inside their box, so text starting exactly at the widget x looks
        // indented one or two pixels to the left of them. Nudge it to line up with the button labels.
        int indent = 4;
        context.drawTextWithShadow(this.textRenderer,
                Text.literal(this.textRenderer.trimToWidth(text, w - indent)), x + indent, y, colour);
    }

    /** The health text has to fit beside the check button, so it is cut to the space left. */
    private String trimStatus(String s) {
        int checkW = Math.min(96, contentWidth / 3);
        int room = Math.max(24, contentWidth - checkW - 10);
        int maxChars = Math.max(6, room / 6);
        return s.length() <= maxChars ? s : s.substring(0, Math.max(1, maxChars - 1)) + "…";
    }

    /**
     * Draws the progress bar. A negative value means the step has no known total, so the bar shows a
     * segment sweeping across instead of sitting empty, which is what an unknown progress looks like
     * to a player otherwise: frozen.
     */
    private void drawBar(DrawContext context, int x, int y, int width, float progress) {
        context.fill(x, y, x + width, y + 5, 0xFF000000);
        if (progress < 0f) {
            // One fifth of the width, bouncing left to right and back.
            int segment = Math.max(12, width / 5);
            int span = Math.max(1, width - segment);
            float phase = (System.currentTimeMillis() % 1400L) / 1400f;
            // Triangle wave: 0 -> 1 -> 0, so the sweep is continuous rather than restarting.
            float t = phase < 0.5f ? phase * 2f : (1f - phase) * 2f;
            int start = x + (int) (span * t);
            context.fill(start, y, start + segment, y + 5, 0xFF55FF55);
            return;
        }
        int filled = (int) (width * Math.max(0f, Math.min(1f, progress)));
        context.fill(x, y, x + filled, y + 5, 0xFF55FF55);
    }

    /** "..." that grows and shrinks, so a phase with no percentage still looks alive. */
    private static String animatedDots() {
        int frame = (int) ((System.currentTimeMillis() / 450L) % 4L);
        return ".".repeat(frame);
    }

    private static String percent(float value) {
        return String.format(java.util.Locale.ROOT, "%.0f%%", Math.max(0f, Math.min(1f, value)) * 100f);
    }

    private static String trim(String s) {
        return s.length() <= 60 ? s : s.substring(0, 59) + "…";
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    /**
     * A slider over a plain number.
     *
     * <p>{@code SliderWidget} stores its value in [0,1]; the mapping to the real range lives here so
     * the caller only deals with the units the config file uses.
     */
    private final class ValueSlider extends SliderWidget {

        private final String label;
        private final double min;
        private final double max;
        private final int decimals;
        private final Consumer<Double> setter;
        private final Runnable afterChange;
        private boolean applying;

        ValueSlider(int x, int y, int width, int height, String label, double value,
                double min, double max, int decimals, Consumer<Double> setter, Runnable afterChange) {
            super(x, y, width, height, Text.literal(label), toFraction(value, min, max));
            this.label = label;
            this.min = min;
            this.max = max;
            this.decimals = decimals;
            this.setter = setter;
            this.afterChange = afterChange;
            updateMessage();
        }

        private static double toFraction(double value, double min, double max) {
            if (max <= min) {
                return 0.0;
            }
            return clamp((value - min) / (max - min), 0.0, 1.0);
        }

        private double actual() {
            return min + this.value * (max - min);
        }

        @Override
        protected void updateMessage() {
            double v = actual();
            String shown = decimals > 0
                    ? String.format(java.util.Locale.ROOT, "%." + decimals + "f", v)
                    : String.valueOf((int) Math.round(v));
            setMessage(Text.literal(label + ": " + shown));
        }

        @Override
        protected void applyValue() {
            if (applying) {
                return;
            }
            // Snapping to the step keeps the config values tidy, and for token counts an integer.
            double v = actual();
            if (decimals <= 0) {
                v = Math.round(v);
            }
            setter.accept(clamp(v, min, max));
        }
    }
}
