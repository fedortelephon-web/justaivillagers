package com.justaivillagers.fabric;

import java.util.ArrayList;
import java.util.List;

import com.justaivillagers.core.Lang;
import com.justaivillagers.core.LangData;
import com.justaivillagers.core.LangPack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

/**
 * A step-by-step guide to getting the mod working, opened from the settings screen.
 *
 * <p>The settings screen can say <em>that</em> the backend is down, but not <em>why</em> it is down
 * in a way a player can act on: installing Ollama, starting the server and pulling a model are three
 * separate jobs, and each fails differently. That belongs on its own screen.
 *
 * <p>The guide is one column of text that scrolls. It was paginated for a while, but how much text
 * fits on a page depends on the window size, the GUI scale and the language at once, and no fixed
 * page break reads well in all of them. Scrolling only ever has one rule: the reader sees a window
 * onto the text and moves it.
 *
 * <p>Copy buttons sit inside the text, next to the address or command they copy. A player following
 * a guide with both hands on the keyboard should not have to hunt for the right button among three
 * that all say "copy".
 *
 * <p>The text sits on an opaque panel. The world behind a screen can be snow, lava or a bright sky,
 * and light grey text over it is unreadable; a solid background is what makes a guide usable rather
 * than merely present.
 */
public final class HelpScreen extends Screen {

    /** Space at the bottom for the row under the panel. */
    private static final int BOTTOM_BAR = 26;

    private static final int LINE = 10;
    private static final int PANEL_TOP = 24;
    private static final int TEXT_TOP = PANEL_TOP + 22;

    /** Height of a copy button row; a button needs a box, so it is taller than a text row. */
    private static final int COPY_LINE = 15;

    /** Width of the scrollbar gutter, drawn only when the text overflows. */
    private static final int SCROLLBAR = 4;

    /** Height of the strip under the text, which the last line slides beneath. */
    private static final int FOOT_STRIP = 8;

    /** Marks a copy button: "#copy|label|value". Values never contain a bar, labels are translated. */
    private static final String COPY = "#copy|";

    private final Screen parent;

    /** Wrapped lines with style, rebuilt when the window width changes. */
    private final List<Line> lines = new ArrayList<>();

    /** Top of every line, in guide coordinates, computed from the line heights. */
    private int[] tops = new int[0];

    /** Total height of the guide, which is what the scroll is measured against. */
    private int totalHeight;

    /** Window width the text was wrapped for; a resize has to re-wrap it. */
    private int builtWidth = -1;

    /** Where the reader wants the text to be, in pixels from the top of the guide. */
    private double targetScroll;

    /** Where it is drawn this frame; eased toward the target so a wheel notch glides. */
    private double drawnScroll;

    /** Copy buttons hit boxes for this frame: x1, y1, x2, y2, index into copyValues. */
    private final List<int[]> hitBoxes = new ArrayList<>();
    private final List<String> copyValues = new ArrayList<>();

    private static final class Line {
        private final OrderedText text;
        private final int colour;
        private final int indent;
        private final int height;
        private final boolean button;
        private final String copyValue;

        private Line(OrderedText text, int colour, int indent, int height,
                     boolean button, String copyValue) {
            this.text = text;
            this.colour = colour;
            this.indent = indent;
            this.height = height;
            this.button = button;
            this.copyValue = copyValue;
        }
    }

    public HelpScreen(Screen parent) {
        super(Text.literal("Just AI Villagers"));
        this.parent = parent;
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
        // Minecraft calls init() again after a window resize, and the wrap width changes with it,
        // so the text has to be rebuilt; keeping the old lines lets them overflow the panel.
        if (lines.isEmpty() || builtWidth != contentWidth()) {
            buildLines();
            builtWidth = contentWidth();
        }
        clampScroll();

        addDrawableChild(ButtonWidget.builder(Text.literal(t("help.close")), b -> close())
                .dimensions(this.width - 84, 6, 76, 18).build());
    }

    /**
     * Puts text on the clipboard and confirms it in chat.
     *
     * <p>Chat is how the settings screen already reports a copy, and the line stays long enough to
     * read; a toast would be gone before a player who is reading the guide looks back.
     */
    private void copyToClipboard(String value) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }
        client.keyboard.setClipboard(value);
        if (client.player != null) {
            client.player.sendMessage(
                    Text.literal("§8[justaivillagers] §a" + t("help.copied")), false);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
        // A notch moves three rows, but only the target moves: render glides toward it, so the text
        // follows the wheel instead of jumping a whole line at a time.
        targetScroll -= vertical * LINE * 3;
        clampScroll();
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (int[] box : hitBoxes) {
                if (mouseX >= box[0] && mouseX < box[2] && mouseY >= box[1] && mouseY < box[3]) {
                    copyToClipboard(copyValues.get(box[4]));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void clampScroll() {
        double max = maxScroll();
        targetScroll = Math.max(0, Math.min(targetScroll, max));
        drawnScroll = Math.max(0, Math.min(drawnScroll, max));
    }

    private int textBottom() {
        return this.height - BOTTOM_BAR - 4 - FOOT_STRIP;
    }

    private int visibleHeight() {
        return Math.max(LINE, textBottom() - TEXT_TOP);
    }

    private double maxScroll() {
        return Math.max(0, totalHeight - visibleHeight());
    }

    @Override
    public void close() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.setScreen(parent);
        }
    }

    /** Splits every section into wrapped lines; "## " marks a heading, "• " a bullet. */
    private void buildLines() {
        lines.clear();
        String[] keys = {
                "help.intro",
                "help.step1", "help.step2", "help.step3", "help.step4", "help.ifnot",
        };
        int width = contentWidth() - 16;
        for (String key : keys) {
            String body = t(key);
            if (body == null || body.startsWith("help.")) {
                continue;
            }
            boolean any = false;
            for (String raw : body.split("\n")) {
                String line = raw.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith(COPY)) {
                    String rest = line.substring(COPY.length());
                    int bar = rest.indexOf('|');
                    String label = bar < 0 ? rest : rest.substring(0, bar);
                    String value = bar < 0 ? rest : rest.substring(bar + 1);
                    lines.add(new Line(Text.literal(label).asOrderedText(), 0xFF1B1D22, 0,
                            COPY_LINE, true, value.trim()));
                    any = true;
                    continue;
                }
                boolean heading = line.startsWith("## ");
                boolean bullet = line.startsWith("• ");
                int colour = heading ? 0xFFD54F : 0xE8E8E8;
                int indent = heading ? 0 : 8;
                if (heading) {
                    line = line.substring(3);
                }
                List<OrderedText> wrapped = this.textRenderer.wrapLines(
                        Text.literal(line), width - indent);
                for (OrderedText part : wrapped) {
                    lines.add(new Line(part, colour, indent, LINE, false, null));
                }
                any = true;
            }
            // A blank row between sections: without it the headings run together into one wall.
            if (any) {
                lines.add(new Line(Text.literal("").asOrderedText(), 0xFFFFFF, 0, LINE, false, null));
            }
        }

        tops = new int[lines.size()];
        int y = 0;
        for (int i = 0; i < lines.size(); i++) {
            tops[i] = y;
            y += lines.get(i).height;
        }
        totalHeight = y;
    }

    private int contentWidth() {
        return Math.min(440, this.width - 60);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // super.render draws the dimmed background; calling it here as well needs a signature that
        // differs between 1.20.1 and 1.21.11.
        super.render(context, mouseX, mouseY, delta);

        int cx = this.width / 2;
        int x = cx - contentWidth() / 2;
        int panelRight = x + contentWidth() + 12;
        int panelLeft = x - 12;
        int panelBottom = this.height - BOTTOM_BAR - 4;

        // Opaque panel: light grey text over a snowy hill is otherwise invisible.
        context.fill(panelLeft, PANEL_TOP, panelRight, panelBottom, 0xF4121418);

        // Ease toward the target. The factor is per frame, so a fast machine settles faster and a
        // slow one still arrives; either way a notch of the wheel slides rather than teleports.
        drawnScroll += (targetScroll - drawnScroll) * 0.25;
        if (Math.abs(targetScroll - drawnScroll) < 0.5) {
            drawnScroll = targetScroll;
        }

        hitBoxes.clear();
        copyValues.clear();

        // Clip to the text box only. The title and footer strips are drawn over the text, and the
        // clip stops one line short of them on both sides, so a row is cut by the strip's edge as it
        // leaves: it slides under the plate instead of vanishing at an invisible boundary.
        context.enableScissor(panelLeft + 1, TEXT_TOP, panelRight - 1, textBottom());
        for (int i = 0; i < lines.size(); i++) {
            int rowTop = TEXT_TOP + tops[i] - (int) Math.round(drawnScroll);
            if (rowTop + lines.get(i).height < TEXT_TOP) {
                continue;
            }
            if (rowTop > textBottom()) {
                break;
            }
            Line line = lines.get(i);
            if (line.button) {
                int textWidth = this.textRenderer.getWidth(line.text);
                int left = x + 4;
                int top = rowTop + 1;
                int right = left + textWidth + 12;
                int bottom = top + 13;
                // A gold outlined button, matching the guide's frame so it reads as part of the page.
                context.fill(left, top, right, bottom, 0xFF2A2D33);
                context.fill(left, top, right, top + 1, 0xFFFFC107);
                context.fill(left, bottom - 1, right, bottom, 0xFFFFC107);
                context.fill(left, top, left + 1, bottom, 0xFFFFC107);
                context.fill(right - 1, top, right, bottom, 0xFFFFC107);
                context.drawTextWithShadow(this.textRenderer, line.text, left + 6, top + 3, 0xFFD54F);
                copyValues.add(line.copyValue);
                hitBoxes.add(new int[] {left, top, right, bottom, copyValues.size() - 1});
                continue;
            }
            if (!line.text.toString().isEmpty()) {
                context.drawTextWithShadow(this.textRenderer, line.text,
                        x + line.indent, rowTop, line.colour);
            }
        }
        context.disableScissor();

        // The title strip is drawn over the text, never the other way round, and it reaches exactly
        // to the first text row: a gap between the strip and the text would show the sliver of a
        // line that is leaving, which reads as the line snapping out of existence.
        context.fill(panelLeft, PANEL_TOP, panelRight, TEXT_TOP, 0xFF1B1D22);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal(t("help.title")), cx, PANEL_TOP + 7, 0xFFD54F);

        // The same at the bottom, so the last line sinks under the frame as it scrolls out.
        context.fill(panelLeft, textBottom(), panelRight, panelBottom, 0xFF1B1D22);

        // Thin gold rules top and bottom, to frame it without shouting.
        context.fill(panelLeft, PANEL_TOP, panelRight, PANEL_TOP + 1, 0xFFFFC107);
        context.fill(panelLeft, panelBottom - 1, panelRight, panelBottom, 0xFFFFC107);

        // A bar on the right tells the reader there is more text below; harmless when there is not.
        double max = maxScroll();
        if (max > 0) {
            int trackTop = TEXT_TOP;
            int trackHeight = visibleHeight();
            context.fill(x + contentWidth() + 4, trackTop,
                    x + contentWidth() + 4 + SCROLLBAR, trackTop + trackHeight, 0x40FFFFFF);
            int thumbHeight = Math.max(16, (int) ((double) trackHeight * trackHeight / totalHeight));
            int thumbTop = trackTop + (int) ((trackHeight - thumbHeight) * (drawnScroll / max));
            context.fill(x + contentWidth() + 4, thumbTop,
                    x + contentWidth() + 4 + SCROLLBAR, thumbTop + thumbHeight, 0xFFD54F);
        }
    }
}
