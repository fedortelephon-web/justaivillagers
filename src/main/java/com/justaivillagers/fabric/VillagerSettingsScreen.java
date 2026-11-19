package com.justaivillagers.fabric;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

/**
 * The mod's settings screen, opened with a keybind or from the mod list.
 *
 * <p>Everything the JSON file can express is reachable here, because a player who has to edit JSON
 * to change the model is a player who will not change the model. Settings are edited on a copy and
 * only written on Save, so Cancel really cancels.
 *
 * <p>Three tabs keep each page short enough to fit on a small window without scrolling: the model
 * page, the behaviour page and the language page.
 */
public final class VillagerSettingsScreen extends Screen {

    private static final int TAB_MODEL = 0;
    private static final int TAB_BEHAVIOUR = 1;
    private static final int TAB_LANGUAGE = 2;

    /** Ollama's language list, in the order the language tab cycles through them. */
    private static final String[] LANG_CHOICES = {
            "", Lang.EN, Lang.RU, Lang.ES, Lang.PT, Lang.DE, Lang.PL, Lang.FR, Lang.IT, Lang.TR,
    };

    private final Screen parent;
    private final ModConfig draft;
    private int tab = TAB_MODEL;

    private TextFieldWidget urlField;
    private TextFieldWidget customModelField;

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
        clearChildren();
        build();
    }

    private void build() {
        int cx = this.width / 2;
        int top = 34;

        // Tabs across the top.
        int tabWidth = Math.min(110, (this.width - 20) / 3);
        String[] labels = { t("settings.tab.model"), t("settings.tab.features"), t("settings.tab.language") };
        for (int i = 0; i < 3; i++) {
            final int index = i;
            int x = cx - (tabWidth * 3 + 8) / 2 + i * (tabWidth + 4);
            ButtonWidget button = ButtonWidget.builder(Text.literal(labels[i]), b -> {
                tab = index;
                rebuild();
            }).dimensions(x, 8, tabWidth, 20).build();
            // The active tab is not clickable, which makes it read as "you are here".
            button.active = tab != index;
            addDrawableChild(button);
        }

        if (tab == TAB_MODEL) {
            buildModelTab(top);
        } else if (tab == TAB_BEHAVIOUR) {
            buildBehaviourTab(top);
        } else {
            buildLanguageTab(top);
        }

        // Save / Cancel pinned to the bottom.
        int buttonY = this.height - 28;
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.save")), b -> save())
                .dimensions(cx - 154, buttonY, 100, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.cancel")), b -> close())
                .dimensions(cx - 50, buttonY, 100, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.close")), b -> close())
                .dimensions(cx + 54, buttonY, 100, 20).build());
    }

    // ---------------------------------------------------------------- model tab

    private void buildModelTab(int top) {
        int cx = this.width / 2;
        int fieldWidth = Math.min(280, this.width - 120);
        int x = cx - fieldWidth / 2;

        // Server address.
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.server")), b -> {
        }).dimensions(x, top, 110, 20).build()).active = false;

        this.urlField = new TextFieldWidget(this.textRenderer, x + 114, top, fieldWidth, 20,
                Text.literal(t("settings.server")));
        this.urlField.setMaxLength(200);
        this.urlField.setText(draft.baseUrl);
        addDrawableChild(this.urlField);

        // Health line plus a recheck button.
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.check")), b -> {
            health = ModRuntime.get().healthNow();
            statusLine = healthLabel();
            rebuild();
        }).dimensions(x, top + 26, 110, 20).build());

        // Model buttons: one per catalogue entry, cheapest first.
        int rowY = top + 56;
        for (ModelCatalog.Entry entry : ModelCatalog.ENTRIES) {
            final ModelCatalog.Entry target = entry;
            String label = entry.label() + " - " + t("settings.model.needs",
                    ModelCatalog.sizeLabel(entry), ModelCatalog.ramLabel(entry));
            if (label.length() > 46) {
                label = label.substring(0, 45) + "…";
            }
            ButtonWidget button = ButtonWidget.builder(
                    Text.literal((entry.id.equals(draft.model) ? "> " : "") + label),
                    b -> {
                        draft.model = target.id;
                        rebuild();
                    }).dimensions(x - 40, rowY, fieldWidth + 40, 20).build();
            addDrawableChild(button);

            // A separate download button per row, so the player does not first have to select it.
            boolean isPulling = pullingModel != null && pullingModel.equals(entry.id);
            String action = isPulling ? "..." : t("settings.model.download");
            ButtonWidget download = ButtonWidget.builder(Text.literal(action), b -> startPull(target.id))
                    .dimensions(x + fieldWidth + 6, rowY, 76, 20).build();
            download.active = !pullFinished && pullingModel == null;
            addDrawableChild(download);
            rowY += 22;
        }

        // Custom model id, for anything not in the catalogue.
        int customY = rowY + 4;
        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.model.custom")), b -> {
        }).dimensions(x, customY, 110, 20).build()).active = false;
        this.customModelField = new TextFieldWidget(this.textRenderer, x + 114, customY, fieldWidth, 20,
                Text.literal(t("settings.model.custom")));
        this.customModelField.setMaxLength(120);
        this.customModelField.setText(draft.model);
        addDrawableChild(this.customModelField);

        // Ollama install helper, shown only when the server is unreachable.
        int helperY = customY + 26;
        if (health != null && !health.available) {
            addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.ollama.download")), b -> startInstaller())
                    .dimensions(x, helperY, fieldWidth / 2 - 2, 20).build());
            addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.ollama.page")), b -> openPage())
                    .dimensions(x + fieldWidth / 2 + 2, helperY, fieldWidth / 2 - 2, 20).build());
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

    private void buildBehaviourTab(int top) {
        int cx = this.width / 2;
        int w = Math.min(320, this.width - 40);
        int x = cx - w / 2;
        int y = top;

        y = toggle(x, y, w, "settings.spontaneous", () -> draft.enableSpontaneousTalk,
                v -> draft.enableSpontaneousTalk = v);
        y = toggle(x, y, w, "settings.group", () -> draft.enableGroupDialogues,
                v -> draft.enableGroupDialogues = v);
        y = toggle(x, y, w, "settings.reactions", () -> draft.enableEventReactions,
                v -> draft.enableEventReactions = v);
        y = toggle(x, y, w, "settings.fallback", () -> draft.enableFallbackLines,
                v -> draft.enableFallbackLines = v);
        y = toggle(x, y, w, "settings.debug", () -> draft.debugPrompts,
                v -> draft.debugPrompts = v);

        y += 4;
        y = number(x, y, w, "settings.temperature", () -> draft.temperature,
                v -> draft.temperature = v, 0.1, 0.0, 2.0, 1);
        y = number(x, y, w, "settings.maxTokens", () -> (double) draft.maxTokens,
                v -> draft.maxTokens = (int) Math.round(v), 20, 16, 512, 0);
        y = number(x, y, w, "settings.speechPace", () -> (double) draft.speechPaceMsPerChar,
                v -> draft.speechPaceMsPerChar = (int) Math.round(v), 10, 0, 300, 0);
        number(x, y, w, "settings.history", () -> (double) draft.maxHistoryMessages,
                v -> draft.maxHistoryMessages = (int) Math.round(v), 2, 2, 40, 0);
    }

    private int toggle(int x, int y, int w, String key, Supplier<Boolean> get,
            java.util.function.Consumer<Boolean> set) {
        String mark = get.get() ? "[x] " : "[ ] ";
        addDrawableChild(ButtonWidget.builder(Text.literal(mark + t(key)), b -> {
            set.accept(!get.get());
            rebuild();
        }).dimensions(x, y, w, 20).build());
        return y + 22;
    }

    /** A "- value +" row, which is the smallest widget set that works on every window size. */
    private int number(int x, int y, int w, String key, Supplier<Double> get,
            java.util.function.Consumer<Double> set, double step, double min, double max, int decimals) {
        addDrawableChild(ButtonWidget.builder(Text.literal("-"), b -> {
            set.accept(clamp(get.get() - step, min, max));
            rebuild();
        }).dimensions(x, y, 20, 20).build());

        String value = decimals > 0
                ? String.format(java.util.Locale.ROOT, "%." + decimals + "f", get.get())
                : String.valueOf((int) Math.round(get.get()));
        addDrawableChild(ButtonWidget.builder(Text.literal(t(key) + ": " + value), b -> {
        }).dimensions(x + 22, y, w - 44, 20).build()).active = false;

        addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> {
            set.accept(clamp(get.get() + step, min, max));
            rebuild();
        }).dimensions(x + w - 20, y, 20, 20).build());
        return y + 22;
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    // ------------------------------------------------------------- language tab

    private void buildLanguageTab(int top) {
        int cx = this.width / 2;
        int w = Math.min(320, this.width - 40);
        int x = cx - w / 2;

        addDrawableChild(ButtonWidget.builder(Text.literal(t("settings.language") + ": " + langLabel()), b -> {
            draft.languageOverride = nextLanguage(draft.languageOverride, 1);
            rebuild();
        }).dimensions(x, top, w, 20).build());

        int y = top + 30;
        // Every supported language as its own button: cycling ten languages one click at a time is
        // worse than seeing them all.
        for (String code : LANG_CHOICES) {
            String label = code.isEmpty() ? t("settings.language.auto") : nameOf(code);
            ButtonWidget button = ButtonWidget.builder(
                    Text.literal((draft.languageOverride.equals(code) ? "> " : "") + label), b -> {
                        draft.languageOverride = code;
                        rebuild();
                    }).dimensions(x, y, w, 20).build();
            addDrawableChild(button);
            y += 22;
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
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 2, 0xFFFFFF);

        int cx = this.width / 2;
        int fieldWidth = Math.min(280, this.width - 120);
        int x = cx - fieldWidth / 2;

        if (tab == TAB_MODEL) {
            context.drawTextWithShadow(this.textRenderer, Text.literal(healthLabel()),
                    x, 34 - 12, health != null && health.available ? 0x55FF55 : 0xFF5555);

            int rowY = 34 + 56 + ModelCatalog.ENTRIES.size() * 22 + 4;
            int lineY = rowY + 26 + 12;

            if (pullingModel != null) {
                float progress = pullTotal > 0 ? (float) pullDone / (float) pullTotal : 0f;
                // A model can be several blobs, so the percentage legitimately restarts; the status
                // text says which phase is running so the bar does not look stuck or broken.
                String text = t("settings.model.downloading", percent(progress));
                if (!pullStatus.isEmpty()) {
                    text = text + "  " + trim(pullStatus);
                }
                context.drawTextWithShadow(this.textRenderer, Text.literal(trim(text)), x, lineY, 0xFFFFFF);
                drawBar(context, x, lineY + 11, fieldWidth, progress);
            } else if (!pullStatus.isEmpty()) {
                context.drawTextWithShadow(this.textRenderer, Text.literal(trim(pullStatus)), x, lineY, 0xFFAA00);
            }

            if (installer != null) {
                float progress = installerTotal > 0 ? (float) installerDone / (float) installerTotal : 0f;
                String text = t("settings.model.downloading", percent(progress));
                context.drawTextWithShadow(this.textRenderer, Text.literal(text), x, lineY + 26, 0xFFFFFF);
                drawBar(context, x, lineY + 37, fieldWidth, progress);
            } else if (installerSaved != null) {
                String text = t("settings.ollama.saved", installerSaved.getFileName().toString());
                context.drawTextWithShadow(this.textRenderer, Text.literal(trim(text)), x, lineY + 26, 0x55FF55);
            } else if (!installerError.isEmpty()) {
                context.drawTextWithShadow(this.textRenderer, Text.literal(trim(installerError)),
                        x, lineY + 26, 0xFF5555);
            }
        }
    }

    private void drawBar(DrawContext context, int x, int y, int width, float progress) {
        context.fill(x, y, x + width, y + 6, 0xFF000000);
        int filled = (int) (width * Math.max(0f, Math.min(1f, progress)));
        context.fill(x, y, x + filled, y + 6, 0xFF55FF55);
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
}
