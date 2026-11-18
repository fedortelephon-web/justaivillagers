package com.justaivillagers.core;

import java.util.List;
import java.util.Map;

/**
 * Everything the mod says in one language: the prompt scaffolding, the canned fallback lines and the
 * small word lists (times of day, weather, professions, speech styles, biomes).
 *
 * <p>Text is stored in two maps rather than forty separate fields. A missing key is a programming
 * error, so it fails loudly in the tests instead of silently producing a prompt with a hole in it.
 */
public final class LangPack {

    /** Language code, one of {@link Lang#ALL}. */
    public final String code;

    /** Prompt scaffolding. Values may contain {@code %s} / {@code %d} placeholders. */
    public final Map<String, String> text;

    /** Canned lines for when the model is unavailable, keyed by situation. */
    public final Map<String, List<String>> fallbacks;

    /** Profession id to the word a person would use for it. */
    public final Map<String, String> professions;

    /** Speech style key to a description usable inside a prompt. */
    public final Map<String, String> styles;

    /** Biome id to a localised name. Unknown biomes fall back to the raw id. */
    public final Map<String, String> biomes;

    /** Subjects a villager may mutter about, so the same thought is not repeated forever. */
    public final List<String> topics;

    /** Time of day, indexed like {@link WorldContext.TimeOfDay}. */
    public final List<String> times;

    /** Weather, indexed like {@link WorldContext.Weather}. */
    public final List<String> weathers;

    /**
     * Profession id to a block of context: the trade a villager practises and the items it may
     * mention. Without this the model offered crocodile leather to a leatherworker, because
     * "mention only things that fit your trade" leaves it to guess what fits.
     */
    public final Map<String, String> trades;

    LangPack(String code, Map<String, String> text, Map<String, List<String>> fallbacks,
             Map<String, String> professions, Map<String, String> styles, Map<String, String> biomes,
             List<String> topics, List<String> times, List<String> weathers,
             Map<String, String> trades) {
        this.topics = List.copyOf(topics);
        this.code = code;
        this.text = Map.copyOf(text);
        this.fallbacks = Map.copyOf(fallbacks);
        this.professions = Map.copyOf(professions);
        this.styles = Map.copyOf(styles);
        this.biomes = Map.copyOf(biomes);
        this.times = List.copyOf(times);
        this.weathers = List.copyOf(weathers);
        this.trades = Map.copyOf(trades);
    }

    /**
     * A prompt fragment, with {@code %s} / {@code %d} substituted.
     *
     * <p>Falls back to the raw key so a missing translation shows up as an obvious {@code reply_answer}
     * in the prompt rather than crashing the game mid-conversation.
     */
    public String t(String key, Object... args) {
        String pattern = text.get(key);
        if (pattern == null) {
            return key;
        }
        return args.length == 0 ? pattern : String.format(pattern, args);
    }

    public boolean has(String key) {
        return text.containsKey(key);
    }

    public List<String> fallback(String key) {
        List<String> lines = fallbacks.get(key);
        return lines == null ? fallbacks.getOrDefault("neutral", List.of()) : lines;
    }

    public String profession(String id) {
        if (id == null) {
            return professions.getOrDefault("villager", "villager");
        }
        return professions.getOrDefault(id, id);
    }

    public String style(String key) {
        return styles.getOrDefault(key, styles.getOrDefault("casual", key));
    }

    public String biome(String id) {
        if (id == null) {
            return "";
        }
        return biomes.getOrDefault(id, id);
    }

    /** The trade block for a profession, or an empty string when there is none. */
    public String trade(String id) {
        if (id == null) {
            return trades.getOrDefault("villager", "");
        }
        return trades.getOrDefault(id, trades.getOrDefault("villager", ""));
    }

    /** Time of day as a word. The enum is ordered MORNING, DAY, EVENING, SUNSET, NIGHT, DAWN. */
    public String time(WorldContext.TimeOfDay time) {
        int index = time == null ? 1 : time.ordinal();
        if (index < 0 || index >= times.size()) {
            index = 1;
        }
        return times.get(index);
    }

    /** Weather as a word. The enum is ordered CLEAR, RAIN, THUNDER. */
    public String weather(WorldContext.Weather weather) {
        int index = weather == null ? 0 : weather.ordinal();
        if (index < 0 || index >= weathers.size()) {
            index = 0;
        }
        return weathers.get(index);
    }
}
