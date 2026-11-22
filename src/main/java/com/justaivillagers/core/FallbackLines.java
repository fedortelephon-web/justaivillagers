package com.justaivillagers.core;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Canned lines used when the model is unavailable or still loading. The original said nothing at
 * all in that case, which reads as "the mod is broken"; a short in-character line keeps the world
 * alive with zero latency.
 *
 * <p>All wording comes from {@link LangPack}, so every supported language has its own lines.
 */
public final class FallbackLines {

    private FallbackLines() {
    }

    public static String forReply(String lang, VillagerProfile profile, WorldContext ctx) {
        LangPack pack = pack(lang);
        if (profile.child) {
            return pick(pack.fallback("child"));
        }
        if (profile.stutters) {
            return pick(pack.fallback("stutter"));
        }
        return pick(pack.fallback(isNegative(profile.personality) ? "grumpy" : "neutral"));
    }

    public static String forReaction(String lang, String eventKey, VillagerProfile profile, String mobName) {
        LangPack pack = pack(lang);
        boolean child = profile != null && profile.child;
        if ("hurt".equals(eventKey)) {
            return pick(pack.fallback(child ? "hurt_child" : "hurt_adult"));
        }
        if ("flee".equals(eventKey)) {
            // Children have a father and no mother, so they call for dad, not for help in general.
            // The canned lines used to name a zombie no matter what was chasing, which read as a
            // plain bug next to a skeleton or a creeper; when the caller knows the mob, its name is
            // substituted instead.
            if (mobName != null && !mobName.isBlank()) {
                String line = pack.t(child ? "flee_child_line" : "flee_adult_line", capitalise(mobName));
                if (!line.isEmpty() && !line.startsWith("flee_")) {
                    return line;
                }
            }
            return pick(pack.fallback(child ? "flee_child" : "flee_adult"));
        }
        return pick(pack.fallback("generic"));
    }

    /** "зомби" -> "Зомби", so the name reads as a word at the start of a shout. */
    private static String capitalise(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.substring(0, 1).toUpperCase(java.util.Locale.ROOT) + text.substring(1);
    }

    public static String forSleeping(String lang) {
        return pick(pack(lang).fallback("sleeping"));
    }

    public static String forGroupLine(String lang, VillagerProfile speaker, WorldContext ctx) {
        LangPack pack = pack(lang);
        if (ctx.time == WorldContext.TimeOfDay.NIGHT) {
            return pick(pack.fallback("group_night"));
        }
        return pick(pack.fallback("group_day"));
    }

    public static String forSpontaneous(String lang, VillagerProfile profile, WorldContext ctx) {
        LangPack pack = pack(lang);
        if (profile.child) {
            return pick(pack.fallback("child_mutter"));
        }
        switch (ctx.weather) {
            case RAIN:
                return pick(pack.fallback("rain"));
            case THUNDER:
                return pick(pack.fallback("thunder"));
            case CLEAR:
            default:
                break;
        }
        return pick(pack.fallback("clear"));
    }

    private static LangPack pack(String lang) {
        return LangData.of(Lang.normalise(lang));
    }

    static boolean isNegative(String personality) {
        if (personality == null) {
            return false;
        }
        String p = personality.toLowerCase(java.util.Locale.ROOT);
        // The trait words themselves are localised, so every language's wording has to be recognised.
        String[] markers = {
                "evil", "greedy", "boaster", "lazy", "grumpy", "skeptic", "envious", "sullen", "arrogant",
                "злой", "скуп", "хваст", "лент", "ворч", "скептик", "завист", "угрюм", "высокомер", "каприз",
                "malvado", "avar", "fanfarr", "perezos", "gruñón", "gruñon", "escéptic", "envidios",
                "hurañ", "huran", "arrogante", "quejic", "miedos",
                "malvado", "avarent", "fanfarr", "preguiços", "preguicos", "rabugent", "céti", "ceti",
                "invejos", "arrogante", "queixos", "medros",
                "böse", "bos", "geizig", "angeber", "faul", "mürrisch", "murrisch", "skeptisch",
                "neidisch", "grantig", "arrogant", "nörgel", "norgel", "ängstlich", "angstlich",
                "méchant", "mechant", "avare", "vantard", "paresseux", "grognon", "sceptique",
                "envieux", "maussade", "arrogant", "pleurnich", "craintif"
        };
        for (String marker : markers) {
            if (p.contains(marker)) {
                return true;
            }
        }
        return false;
    }

    private static String pick(List<String> lines) {
        if (lines.isEmpty()) {
            return "";
        }
        return lines.get(ThreadLocalRandom.current().nextInt(lines.size()));
    }
}
