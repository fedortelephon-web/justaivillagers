package com.justaivillagers.core;

import java.util.List;

/**
 * The small models the mod offers in its settings screen.
 *
 * <p>Sizes are the real download sizes from the Ollama library, not guesses: a player deciding
 * whether to pull a model needs to know what it costs before the download starts, and "about a few
 * gigabytes" is not an answer. Every entry is a model small enough to run on a normal desktop next
 * to Minecraft, which is the only kind the mod should recommend.
 *
 * <p>Ordered by download size. {@code quality} is a 1-5 score for this specific job - short, in
 * character villager lines - not a general benchmark ranking.
 */
public final class ModelCatalog {

    /** One selectable model. */
    public static final class Entry {

        /** Ollama model id, exactly as used by {@code ollama pull}. */
        public final String id;

        /** Download size in megabytes, from the Ollama library page. */
        public final int sizeMb;

        /** Rough RAM the model needs while loaded, in megabytes. */
        public final int ramMb;

        /** How well it writes short villager lines, 1-5. */
        public final int quality;

        public Entry(String id, int sizeMb, int ramMb, int quality) {
            this.id = id;
            this.sizeMb = sizeMb;
            this.ramMb = ramMb;
            this.quality = quality;
        }

        /** "gemma3:1b" -> "Gemma 3 1B", for the button label. */
        public String label() {
            String name = id;
            String tag = "";
            int colon = id.indexOf(':');
            if (colon > 0) {
                name = id.substring(0, colon);
                tag = id.substring(colon + 1);
            }
            name = name.replace("qwen2.5", "Qwen2.5").replace("qwen3", "Qwen3")
                    .replace("gemma3", "Gemma 3").replace("llama3.2", "Llama 3.2")
                    .replace("phi3.5", "Phi 3.5").replace("mistral", "Mistral");
            // A custom id is shown as the player typed it, but a bare lowercase family name reads as
            // a typo in a button, so capitalise the first letter only.
            if (!name.isEmpty() && Character.isLowerCase(name.charAt(0))) {
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            }
            if (tag.isEmpty()) {
                return name;
            }
            return name + " " + tag.toUpperCase(java.util.Locale.ROOT);
        }
    }

    /**
     * The catalogue, cheapest first.
     *
     * <p>All five fit comfortably on 8 GB of RAM. The largest, gemma3:4b, is the mod's default
     * because it is the smallest model that reliably stays in character across nine languages;
     * the tiny ones are for players whose machine cannot carry 4B.
     */
    public static final List<Entry> ENTRIES = List.of(
            // 398 MB, 494M params, Apache 2.0. Fast but forgetful; fine for short lines.
            new Entry("qwen2.5:0.5b", 398, 900, 2),
            // 523 MB, 752M params, Apache 2.0. Stronger than its size suggests, 100+ languages.
            new Entry("qwen3:0.6b", 523, 1100, 3),
            // 806 MB, 1B params, Gemma licence. The best quality per megabyte here.
            new Entry("gemma3:1b", 806, 1500, 4),
            // 1.3 GB, 1.24B params, Llama licence. Good English, weaker on the other eight.
            new Entry("llama3.2:1b", 1300, 2000, 3),
            // 3.4 GB, 3.88B params, Gemma licence. The default: best lines, needs ~5 GB free.
            new Entry("gemma3:4b", 3400, 5200, 5)
    );

    /** The model the mod ships with. */
    public static final String DEFAULT_ID = "gemma3:4b";

    private ModelCatalog() {
    }

    /** The entry for an id, or {@code null} when the player typed something custom. */
    public static Entry find(String id) {
        if (id == null) {
            return null;
        }
        for (Entry e : ENTRIES) {
            if (e.id.equals(id)) {
                return e;
            }
        }
        return null;
    }

    /** Human-readable size, either "398 MB" or "3.4 GB". */
    public static String sizeLabel(Entry entry) {
        if (entry.sizeMb < 1000) {
            return entry.sizeMb + " MB";
        }
        return String.format(java.util.Locale.ROOT, "%.1f GB", entry.sizeMb / 1024.0);
    }

    /** "Needs about 5.2 GB of RAM". */
    public static String ramLabel(Entry entry) {
        if (entry.ramMb < 1000) {
            return entry.ramMb + " MB";
        }
        return String.format(java.util.Locale.ROOT, "%.1f GB", entry.ramMb / 1024.0);
    }
}
