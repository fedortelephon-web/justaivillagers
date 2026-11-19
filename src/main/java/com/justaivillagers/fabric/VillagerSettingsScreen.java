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

    /** Model download in flight, if any. */
    private ModelPuller puller;
    private String pullingModel;
    private long pullDone;
    private long pullTotal;
    private String pullStatus = "";
    private boolean pullFinished;

    /** Set by a listener when the widget state must change; tick() performs the rebuild. */
    private volatile boolean needsRebuild;

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
        // open with a short timeout; the backend caches it so later opens are instant.
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

        // Save / Cancel pinned to the bottom, never overlapping the content area.
        int buttonY = this.height - BOTTOM_BAR + 3;
        int bw = Math.min(100, (this.width - 16) / 3);
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.save")), b -> save())
                .dimensions(cx - bw - 4 - bw / 2 - 2, buttonY, bw, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.cancel")), b -> close())
                .dimensions(cx - bw / 2, buttonY, bw, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.close")), b -> close())
                .dimensions(cx + bw / 2 + 6, buttonY, bw, 20).build());
    }

    /** How many rows the active tab needs; used only to size rows before laying them out. */
    private int rowCount() {
        if (tab == TAB_MODEL) {
            // Address, check, five catalogue models, custom id, Ollama helper.
            return 4 + ModelCatalog.ENTRIES.size() + 1;
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
                rebuild();
            }).dimensions(x, y, w - downloadW - 4, fieldH).build();
            select.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(Text.literal(modelTip(entry))));
            addDrawableChild(select);

            boolean isPulling = pullingModel != null && pullingModel.equals(entry.id);
            String downloadText = isPulling
                    ? "..."
                    : this.textRenderer.trimToWidth(t("settings.model.get"), downloadW - 8);
            ButtonWidget download = ButtonWidget.builder(
                    Text.literal(downloadText),
                    b -> startPull(target.id)).dimensions(x + w - downloadW, y, downloadW, fieldH).build();
            download.active = !pullFinished && pullingModel == null;
            download.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                    Text.literal(t("settings.model.get.tip", entry.id))));
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

        // Ollama helper, shown only when the server is unreachable.
        if (health != null && !health.available) {
            int third = (w - 8) / 3;
            ButtonWidget get = ButtonWidget.builder(
                    Text.literal(t("settings.ollama.download")), b -> startInstaller())
                    .dimensions(x, y, third, fieldH).build();
            get.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                    Text.literal(t("settings.ollama.download.tip"))));
            addDrawableChild(get);

            ButtonWidget page = ButtonWidget.builder(
                    Text.literal(t("settings.ollama.page")), b -> openPage())
                    .dimensions(x + third + 4, y, third, fieldH).build();
            page.setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(
                    Text.literal(com.justaivillagers.core.BackendHint.OLLAMA_URL)));
            addDrawableChild(page);

            // While a download runs the third slot cancels it, so a stuck or unwanted installer does
            // not leave the player waiting for a file they no longer want.
            if (installer != null) {
                ButtonWidget cancel = ButtonWidget.builder(
                        Text.literal(t("settings.cancel")), b -> cancelInstaller())
                        .dimensions(x + 2 * (third + 4), y, third, fieldH).build();
                addDrawableChild(cancel);
            }
            y += rowHeight;
        }

        // Everything below this point is painted text and bars, never widgets, so it starts under
        // the last button instead of sharing its line.
        this.progressY = y;
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
        if (pullingModel != null) {
            return;
        }
        pullingModel = model;
        pullFinished = false;
        pullDone = 0;
        pullTotal = -1;
        pullStatus = "";

        // Pull against the address currently in the field, so a player who just fixed the URL does
        // not have to save first to make the download work.
        ModConfig probe = draft.copy();
        probe.baseUrl = urlField != null ? urlField.getText() : draft.baseUrl;

        ModelPuller active = new ModelPuller(probe, ModRuntime.LOGGER);
        this.puller = active;
        active.pull(model, new ModelPuller.Listener() {
            @Override
            public void onStatus(String text) {
                pullStatus = text;
            }

            @Override
            public void onProgress(long completed, long total) {
                pullDone = completed;
                pullTotal = total;
            }

            @Override
            public void onDone() {
                pullFinished = true;
                needsRebuild = true;
                pullingModel = null;
                draft.model = model;
            }

            @Override
            public void onError(String message) {
                pullStatus = message;
                needsRebuild = true;
                pullingModel = null;
            }
        });
        rebuild();
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

    /**
     * Opens the Ollama download page in the system browser.
     *
     * <p>Two attempts, because neither works everywhere: Minecraft's own opener is the polite path,
     * and the platform command is the fallback for a JVM where it silently refuses. Every outcome is
     * logged, so "the button does nothing" becomes a line in the log instead of a mystery.
     */
    private void openPage() {
        String url = com.justaivillagers.core.BackendHint.OLLAMA_URL;
        boolean opened = false;
        try {
            net.minecraft.util.Util.getOperatingSystem().open(java.net.URI.create(url));
            opened = true;
            ModRuntime.LOGGER.info("Opened {} via Minecraft", url);
        } catch (Exception | Error e) {
            ModRuntime.LOGGER.warn("Minecraft could not open {}: {}", url, e.toString());
        }
        if (opened) {
            return;
        }
        try {
            String os = System.getProperty("os.name", "").toLowerCase(java.util.Locale.ROOT);
            ProcessBuilder pb;
            if (os.contains("win")) {
                pb = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url);
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("open", url);
            } else {
                pb = new ProcessBuilder("xdg-open", url);
            }
            pb.start();
            ModRuntime.LOGGER.info("Opened {} via the platform command", url);
        } catch (Exception | Error e) {
            // Last resort: put the address where the player can copy it.
            ModRuntime.LOGGER.warn("Could not open a browser for {}: {}", url, e.toString());
            if (client != null && client.player != null) {
                client.player.sendMessage(net.minecraft.text.Text.literal("§8[justaivillagers] §e" + url), false);
            }
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
        addDrawableChild(ButtonWidget.builder(Text.literal(mark + t(key)), b -> {
            set.accept(!get.get());
            rebuild();
        }).dimensions(x, y, w, h).build());
        return y + rowHeight;
    }

    /** A real slider: dragging is one gesture instead of dozens of clicks on a plus button. */
    private int slider(int x, int y, int w, int h, String key, Supplier<Double> get,
            Consumer<Double> set, double min, double max, int decimals) {
        final String label = t(key);
        addDrawableChild(new ValueSlider(x, y, w, h, label, get.get(), min, max, decimals, set,
                () -> {
                    // Rebuild so the widget picks up the new value from the supplier.
                    rebuild();
                }));
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
        if (puller != null) {
            puller.cancel();
        }
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
            // The health line sits on the check row, to the right of the button.
            int checkW = Math.min(96, w / 3);
            boolean ok = health != null && health.available;
            context.drawTextWithShadow(this.textRenderer, Text.literal(trimStatus(healthLabel())),
                    x + checkW + 6, contentTop() + rowHeight + 4, ok ? 0x55FF55 : 0xFF5555);

            int lineY = progressY;
            int limit = this.height - BOTTOM_BAR;

            // Model download: one caption, one bar, then four pixels of breathing room.
            if (pullingModel != null) {
                boolean known = pullTotal > 0;
                float progress = known ? (float) pullDone / (float) pullTotal : -1f;
                // A model can be several blobs, so the percentage legitimately restarts; the status
                // text says which phase is running so the bar does not look stuck or broken. Some
                // phases report no total at all, hence the sweep and the dots.
                String text = known
                        ? t("settings.model.downloading", percent(progress))
                        : t("settings.model.downloading", animatedDots());
                if (!pullStatus.isEmpty()) {
                    text = text + "  " + pullStatus;
                }
                drawLine(context, text, x, lineY, w, 0xFFFFFF, limit);
                drawBar(context, x, lineY + 10, w, progress);
                lineY += 18;
            } else if (!pullStatus.isEmpty()) {
                // Nothing is downloading but a phase message is still set: that is a finished or
                // failed step, so show the dots too rather than a sentence that never changes.
                drawLine(context, pullStatus + animatedDots(), x, lineY, w, 0xFFAA00, limit);
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
        return s.length() <= maxChars ? s : s.substring(0, Math.max(1, maxChars - 1)) + "вЂ¦";
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
        return s.length() <= 60 ? s : s.substring(0, 59) + "вЂ¦";
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
