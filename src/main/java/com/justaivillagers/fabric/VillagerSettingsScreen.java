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
            // Address, check, five catalogue models, custom id.
            return 3 + ModelCatalog.ENTRIES.size() + 1;
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

        // Server address: the label doubles as a caption, so it does not need its own row.
        this.urlField = new TextFieldWidget(this.textRenderer, x, y, w, fieldH,
                Text.literal(t("settings.server")));
        this.urlField.setMaxLength(200);
        this.urlField.setText(draft.baseUrl);
        addDrawableChild(this.urlField);
        y += rowHeight;

        // Health line and the recheck button share a row.
        int checkW = Math.min(96, w / 3);
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.check")), b -> {
            health = ModRuntime.get().healthNow();
            statusLine = healthLabel();
            rebuild();
        }).dimensions(x, y, checkW, fieldH).build());
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
            addDrawableChild(select);

            boolean isPulling = pullingModel != null && pullingModel.equals(entry.id);
            String downloadText = isPulling
                    ? "..."
                    : this.textRenderer.trimToWidth(t("settings.model.get"), downloadW - 8);
            ButtonWidget download = ButtonWidget.builder(
                    Text.literal(downloadText),
                    b -> startPull(target.id)).dimensions(x + w - downloadW, y, downloadW, fieldH).build();
            download.active = !pullFinished && pullingModel == null;
            addDrawableChild(download);
            y += rowHeight;
        }

        // Name of the selected model, editable for anything not in the catalogue.
        this.customModelField = new TextFieldWidget(this.textRenderer, x, y, w, fieldH,
                Text.literal(t("settings.model.custom")));
        this.customModelField.setMaxLength(120);
        this.customModelField.setText(draft.model);
        addDrawableChild(this.customModelField);
        y += rowHeight;

        this.progressY = y;

        // Ollama install helper, shown only when the server is unreachable.
        if (health != null && !health.available) {
            int half = w / 2 - 2;
            addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.ollama.download")), b -> startInstaller())
                    .dimensions(x, y, half, fieldH).build());
            addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.ollama.page")), b -> openPage())
                    .dimensions(x + w - half, y, half, fieldH).build());
        }
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

    private void openPage() {
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(OllamaInstaller.DOWNLOAD_PAGE));
            }
        } catch (Exception | Error e) {
            // A browser we cannot open is not worth a crash; the URL is also printed to the log.
            ModRuntime.LOGGER.info("Open {} in a browser", OllamaInstaller.DOWNLOAD_PAGE);
        }
    }

    private String healthLabel() {
        if (health == null) {
            return t("settings.status.down");
        }
        if (health.available) {
            return t("settings.status.ok", draft.model);
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
            case Lang.RU: return "Р СѓСЃСЃРєРёР№";
            case Lang.ES: return "EspaГ±ol";
            case Lang.PT: return "PortuguГЄs";
            case Lang.DE: return "Deutsch";
            case Lang.PL: return "Polski";
            case Lang.FR: return "FranГ§ais";
            case Lang.IT: return "Italiano";
            case Lang.TR: return "TГјrkГ§e";
            default: return code;
        }
    }

    // ------------------------------------------------------------------ actions

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (capturingKey != 0) {
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
        return super.keyPressed(keyCode, scanCode, modifiers);
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
            if (pullingModel != null) {
                float progress = pullTotal > 0 ? (float) pullDone / (float) pullTotal : 0f;
                // A model can be several blobs, so the percentage legitimately restarts; the status
                // text says which phase is running so the bar does not look stuck or broken.
                String text = t("settings.model.downloading", percent(progress));
                if (!pullStatus.isEmpty()) {
                    text = text + "  " + pullStatus;
                }
                context.drawTextWithShadow(this.textRenderer, Text.literal(trim(text)), x, lineY, 0xFFFFFF);
                drawBar(context, x, lineY + 10, w, progress);
            } else if (!pullStatus.isEmpty()) {
                context.drawTextWithShadow(this.textRenderer, Text.literal(trim(pullStatus)), x, lineY, 0xFFAA00);
            }

            if (installer != null) {
                float progress = installerTotal > 0 ? (float) installerDone / (float) installerTotal : 0f;
                context.drawTextWithShadow(this.textRenderer,
                        Text.literal(t("settings.model.downloading", percent(progress))), x, lineY + 22, 0xFFFFFF);
                drawBar(context, x, lineY + 32, w, progress);
            } else if (installerSaved != null) {
                context.drawTextWithShadow(this.textRenderer,
                        Text.literal(trim(t("settings.ollama.saved", installerSaved.getFileName().toString()))),
                        x, lineY + 22, 0x55FF55);
            } else if (!installerError.isEmpty()) {
                context.drawTextWithShadow(this.textRenderer, Text.literal(trim(installerError)),
                        x, lineY + 22, 0xFF5555);
            }
        }
    }

    /** The health text has to fit beside the check button, so it is cut to the space left. */
    private String trimStatus(String s) {
        int checkW = Math.min(96, contentWidth / 3);
        int room = Math.max(24, contentWidth - checkW - 10);
        int maxChars = Math.max(6, room / 6);
        return s.length() <= maxChars ? s : s.substring(0, Math.max(1, maxChars - 1)) + "вЂ¦";
    }

    private void drawBar(DrawContext context, int x, int y, int width, float progress) {
        context.fill(x, y, x + width, y + 5, 0xFF000000);
        int filled = (int) (width * Math.max(0f, Math.min(1f, progress)));
        context.fill(x, y, x + filled, y + 5, 0xFF55FF55);
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
