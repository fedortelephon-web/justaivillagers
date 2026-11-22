package com.justaivillagers.core;

import java.util.List;
import java.util.Locale;

/**
 * Supported languages and the mapping from Minecraft locale codes to them.
 *
 * <p>Language selection is an explicit lookup rather than a binary check: any code that is not in
 * {@link #ALL} falls back to English. Adding a language means one {@code LangDataXx} file and one
 * entry in {@link #ALL}.
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
     * Every supported language, ordered by player count (2026 server-market survey: English 65.2%,
     * Turkish 9.7%, Spanish 4.6%, Portuguese 3.8%, German 2.4%, Polish 1.3%, French 1.3%,
     * Italian 0.8%, Russian 0.2%).
     */
    public static final List<String> ALL = List.of(EN, TR, ES, PT, DE, PL, FR, IT, RU);

    private Lang() {
    }

    /**
     * Maps any locale code to a supported language, falling back to English. Only the part before
     * the separator counts, so {@code pt_br} and {@code pt_pt} share one bundle.
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
