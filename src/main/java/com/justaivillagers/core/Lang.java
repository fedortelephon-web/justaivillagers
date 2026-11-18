package com.justaivillagers.core;

import java.util.List;
import java.util.Locale;

/**
 * The languages the mod speaks, and the mapping from Minecraft's locale codes to them.
 *
 * <p>The mod used to have exactly two languages and decided between them with
 * {@code "en".equals(lang)} in a dozen places. That is why a German, French or Spanish client used to
 * get Russian villagers: only English matched "English", and everything else fell through to the
 * other branch.
 *
 * <p>Ordered by how many Minecraft players actually use the language, which is why English, Russian,
 * Spanish, Portuguese, German and French come first. Adding a language is now one file
 * ({@code LangDataXx}) plus one entry in {@link #ALL}.
 */
public final class Lang {

    public static final String EN = "en";
    public static final String RU = "ru";
    public static final String ES = "es";
    public static final String PT = "pt";
    public static final String DE = "de";
    public static final String FR = "fr";
    public static final String TR = "tr";
    public static final String PL = "pl";
    public static final String IT = "it";

    /**
     * Every supported language, ordered by how many Minecraft players actually use it.
     *
     * <p>The order comes from a 2026 server-market survey: English 65.2% of players, Turkish 9.7%,
     * Spanish 4.6%, Portuguese 3.8%, German 2.4%, Polish 1.3%, French 1.3%, Italian 0.8%,
     * Russian 0.2%. Turkish was added late precisely because guessing without data had put French
     * ahead of a language seven times its size.
     */
    public static final List<String> ALL = List.of(EN, TR, ES, PT, DE, PL, FR, IT, RU);

    private Lang() {
    }

    /**
     * Maps any locale code to a supported language, falling back to English.
     *
     * <p>Minecraft reports codes like {@code de_de}, {@code pt_br}, {@code es_mx} or {@code fr_fr};
     * only the part before the separator decides the language, so every regional variant of
     * Portuguese lands on the same bundle. An unknown language gets English rather than a random
     * other one.
     */
    public static String normalise(String languageCode) {
        if (languageCode == null) {
            return EN;
        }
        String lower = languageCode.toLowerCase(Locale.ROOT).replace('-', '_');
        int cut = lower.indexOf('_');
        String base = cut < 0 ? lower : lower.substring(0, cut);
        for (String code : ALL) {
            if (code.equals(base)) {
                return code;
            }
        }
        return EN;
    }

    public static boolean isSupported(String code) {
        return code != null && ALL.contains(code);
    }

    /** The language that should actually be used, never null and never unsupported. */
    public static String resolve(String languageCode) {
        return LangData.of(normalise(languageCode)).code;
    }
}
