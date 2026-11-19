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
 * <p>Content is paginated rather than scrolled. Overriding the scroll hook needs a different
 * signature in each Minecraft version, and paging is what a short guide wants anyway: no text is
 * ever half hidden under a button, and the page counter tells the reader how much is left.
 *
 * <p>The text sits on an opaque panel. The world behind a screen can be snow, lava or a bright sky,
 * and light grey text over it is unreadable; a solid background is what makes a guide usable rather
 * than merely present.
 */
public final class HelpScreen extends Screen {

    /** Space at the bottom for the navigation buttons and the page counter above them. */
    private static final int BOTTOM_BAR = 32;

    /** Height reserved under the panel for the counter plus a little air. */
    private static final int COUNTER_SPACE = BOTTOM_BAR + 18;

    private static final int LINE = 10;
    private static final int PANEL_TOP = 24;

    private final Screen parent;

    /** Wrapped lines with style, built once: wrapping depends on the window width. */
    private final List<Line> lines = new ArrayList<>();

    /** Indexes where a page is allowed to begin. */
    private final List<Integer> breakPoints = new ArrayList<>();

    /** Page boundaries, recomputed only when the wrapped lines change. */
    private List<Integer> startsCache;

    private int page;

    private static final class Line {
        private final OrderedText text;
        private final int colour;
        private final int indent;

        private Line(OrderedText text, int colour, int indent) {
            this.text = text;
            this.colour = colour;
            this.indent = indent;
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
        if (lines.isEmpty()) {
            buildLines();
        }
        int y = this.height - BOTTOM_BAR + 4;
        int bw = Math.min(110, (this.width - 24) / 2);
        int cx = this.width / 2;

        ButtonWidget prev = ButtonWidget.builder(Text.literal(t("help.prev")), b -> turn(-1))
                .dimensions(cx - bw - 4, y, bw, 20).build();
        prev.active = page > 0;
        addDrawableChild(prev);

        ButtonWidget next = ButtonWidget.builder(Text.literal(t("help.next")), b -> turn(1))
                .dimensions(cx + 4, y, bw, 20).build();
        next.active = page < pageCount() - 1;
        addDrawableChild(next);

        // Close sits top-right, clear of both the text and the page navigation.
        ButtonWidget close = ButtonWidget.builder(Text.literal(t("help.close")), b -> close())
                .dimensions(this.width - 84, 6, 76, 18).build();
        addDrawableChild(close);
    }

    /** Changes page and refreshes the buttons' enabled state without losing scroll position. */
    private void turn(int delta) {
        int target = page + delta;
        if (target < 0 || target >= pageCount()) {
            return;
        }
        page = target;
        int keep = page;
        this.clearChildren();
        this.init();
        this.page = keep;
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
        startsCache = null;
        breakPoints.clear();
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
            for (String raw : body.split("\n")) {
                String line = raw.trim();
                if (line.isEmpty()) {
                    continue;
                }
                boolean heading = line.startsWith("## ");
                boolean bullet = line.startsWith("• ");
                int colour = heading ? 0xFFD54F : 0xE8E8E8;
                int indent = heading ? 0 : 8;
                if (heading) {
                    line = line.substring(3);
                }
                // A heading or a bullet may begin a page; a continuation row of a wrapped sentence
                // may not, because splitting a sentence across pages is what makes a guide annoying.
                if (heading || bullet) {
                    breakPoints.add(lines.size());
                }
                List<OrderedText> wrapped = this.textRenderer.wrapLines(
                        Text.literal(line), width - indent);
                for (OrderedText part : wrapped) {
                    lines.add(new Line(part, colour, indent));
                }
            }
            // A blank row ends the section and is always a safe place to break the page.
            breakPoints.add(lines.size());
            lines.add(new Line(Text.literal("").asOrderedText(), 0xFFFFFF, 0));
        }
        breakPoints.add(lines.size());
    }

    private int contentWidth() {
        return Math.min(440, this.width - 60);
    }

    /** Rows that fit between the panel top and the page counter. */
    private int maxLinesPerPage() {
        int panelBottomLimit = this.height - COUNTER_SPACE;
        int available = panelBottomLimit - (PANEL_TOP + 28);
        return Math.max(3, available / LINE);
    }

    /**
     * First line of every page.
     *
     * <p>Pages end at a break point, which is a section gap or the start of a heading or bullet, so
     * a sentence or a list item is never cut in half. Among the break points that fit, the one
     * closest to an even share of the text wins, which keeps the last page from holding a single
     * orphan line.
     */
    private List<Integer> pageStarts() {
        if (startsCache != null) {
            return startsCache;
        }
        int max = maxLinesPerPage();
        int total = lines.size();
        List<Integer> starts = new ArrayList<>();
        starts.add(0);
        if (total > 0) {
            int pages = Math.max(1, (total + max - 1) / max);
            int target = (total + pages - 1) / pages;
            int start = 0;
            while (start < total) {
                int limit = Math.min(total, start + max);
                if (limit >= total) {
                    break;
                }
                int best = -1;
                for (int point : breakPoints) {
                    if (point <= start) {
                        continue;
                    }
                    if (point > limit) {
                        break;
                    }
                    if (point >= start + target || best < 0) {
                        best = point;
                    }
                    if (point >= start + target) {
                        break;
                    }
                }
                if (best <= start) {
                    best = limit;
                }
                starts.add(best);
                start = best;
            }
        }
        startsCache = starts;
        return starts;
    }

    private int pageCount() {
        return pageStarts().size();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // super.render draws the dimmed background; calling it here as well needs a signature that
        // differs between 1.20.1 and 1.21.11.
        super.render(context, mouseX, mouseY, delta);

        int cx = this.width / 2;
        int x = cx - contentWidth() / 2;
        int per = maxLinesPerPage();
        int textTop = PANEL_TOP + 22;
        int panelBottom = textTop + per * LINE + 6;

        // Opaque panel: light grey text over a snowy hill is otherwise invisible.
        context.fill(x - 12, PANEL_TOP, x + contentWidth() + 12, panelBottom, 0xF4121418);
        // Thin gold rules top and bottom, to frame it without shouting.
        context.fill(x - 12, PANEL_TOP, x + contentWidth() + 12, PANEL_TOP + 1, 0xFFFFC107);
        context.fill(x - 12, panelBottom - 1, x + contentWidth() + 12, panelBottom, 0xFFFFC107);
        // A darker strip behind the title, so it does not collide with the world above the panel.
        context.fill(x - 12, PANEL_TOP + 1, x + contentWidth() + 12, textTop - 4, 0xFF1B1D22);

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal(t("help.title")), cx, PANEL_TOP + 7, 0xFFD54F);

        int start = pageStarts().get(Math.min(page, pageCount() - 1));
        // A section gap is a blank row; a page that opens with one wastes the row and reads as a
        // margin bug, so skip any leading gaps.
        while (start < lines.size() && lines.get(start).text.toString().isEmpty()) {
            start++;
        }
        int end = Math.min(lines.size(), start + per);
        int y = textTop;
        for (int i = start; i < end; i++) {
            Line line = lines.get(i);
            if (line.text.toString().isEmpty()) {
                y += LINE;
                continue;
            }
            context.drawTextWithShadow(this.textRenderer, line.text, x + line.indent, y, line.colour);
            y += LINE;
        }

        // The counter goes above the buttons: the bottom centre of the screen belongs to the hotbar.
        String counter = t("help.page", String.valueOf(page + 1), String.valueOf(pageCount()));
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(counter),
                cx, this.height - BOTTOM_BAR - 13, 0xB0B0B0);
    }
}
