package com.justaivillagers.core;

/**
 * Cleans raw model output before it reaches chat: strips markdown emphasis, surrounding quotes,
 * stray JSON escapes, collapses whitespace and cuts the "As an AI language model..." preamble that
 * small models like to add despite the system prompt.
 */
public final class TextSanitizer {

    private static final String[] PREAMBLES = {
            "as an ai language model",
            "as an ai,",
            "as a language model",
            "как языковая модель",
            "как ии",
    };

    private TextSanitizer() {
    }

    public static String cleanForChat(String raw) {
        if (raw == null) {
            return "";
        }
        String text = unescapeJsonRemnants(raw).trim();

        // Small models sometimes wrap the answer in quotes.
        text = stripWrapping(text, '"');
        text = stripWrapping(text, '\u201c', '\u201d');

        // Markdown emphasis and code fences do not render in vanilla chat.
        text = text.replace("**", "").replace("__", "").replace("`", "");

        text = text.replaceAll("\\s+", " ").trim();

        String lower = text.toLowerCase();
        for (String pre : PREAMBLES) {
            if (lower.startsWith(pre)) {
                int idx = text.indexOf(':');
                if (idx > 0 && idx < 80) {
                    text = text.substring(idx + 1).trim();
                } else {
                    text = text.substring(pre.length()).replaceFirst("^[,\\s.]+", "").trim();
                }
                break;
            }
        }

        // Collapse repeated punctuation, e.g. "!!!!!" -> "!"
        text = text.replaceAll("([!?])\\1{2,}", "$1$1$1");

        if (text.length() > 400) {
            text = text.substring(0, 400).trim();
        }
        return text;
    }

    /**
     * Removes a speaker label the model put in front of its own line, like {@code "Стёпа: ..."}.
     *
     * <p>The prompt names the villager, so small models sometimes answer in transcript form and chat
     * then shows the name twice. Only an exact match of this villager's name followed by a colon or
     * a dash is stripped, so ordinary sentences that merely begin with the name survive.
     */
    public static String stripSpeakerPrefix(String text, String name) {
        if (text == null || name == null || name.isEmpty()) {
            return text;
        }
        String bare = text.trim();
        // Peel one layer of wrapping quotes, so «Стёпа: ...» is handled like Стёпа: ...
        if (bare.length() >= 2) {
            char first = bare.charAt(0);
            char last = bare.charAt(bare.length() - 1);
            boolean quoted = (first == '"' && last == '"')
                    || (first == '\u201c' && last == '\u201d')
                    || (first == '\u00ab' && last == '\u00bb');
            if (quoted) {
                bare = bare.substring(1, bare.length() - 1).trim();
            }
        }
        if (bare.length() <= name.length() || !bare.regionMatches(true, 0, name, 0, name.length())) {
            return text;
        }
        int after = name.length();
        // Tolerate "Стёпа : ..." and "Стёпа — ..." as well as the plain "Стёпа: ...".
        while (after < bare.length() && Character.isWhitespace(bare.charAt(after))) {
            after++;
        }
        if (after >= bare.length()) {
            return text;
        }
        char separator = bare.charAt(after);
        if (separator != ':' && separator != '\u2014') {
            return text;
        }
        String rest = bare.substring(after + 1).trim();
        return rest.isEmpty() ? text : rest;
    }

    private static String stripWrapping(String text, char quote) {
        return stripWrapping(text, quote, quote);
    }

    private static String stripWrapping(String text, char open, char close) {
        if (text.length() >= 2 && text.charAt(0) == open && text.charAt(text.length() - 1) == close) {
            return text.substring(1, text.length() - 1).trim();
        }
        return text;
    }

    /** Handles the cases where a model echoes JSON escapes into plain text. */
    static String unescapeJsonRemnants(String s) {
        if (s.indexOf('\\') < 0) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '\\' || i + 1 >= s.length()) {
                sb.append(c);
                continue;
            }
            char next = s.charAt(i + 1);
            switch (next) {
                case '"':
                case '\\':
                case '/':
                    sb.append(next);
                    i++;
                    break;
                case 'n':
                    sb.append('\n');
                    i++;
                    break;
                case 't':
                    sb.append('\t');
                    i++;
                    break;
                case 'r':
                    sb.append('\r');
                    i++;
                    break;
                case 'u':
                    if (i + 5 < s.length()) {
                        try {
                            sb.append((char) Integer.parseInt(s.substring(i + 2, i + 6), 16));
                            i += 5;
                        } catch (NumberFormatException e) {
                            sb.append(c);
                        }
                    } else {
                        sb.append(c);
                    }
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
