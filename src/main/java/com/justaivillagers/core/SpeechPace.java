package com.justaivillagers.core;

/**
 * Turns a line of text into a pause that matches how long a person would take to say it.
 *
 * <p>The model replies in a fraction of a second, so without pacing an evening dialogue between two
 * villagers lands in chat as a wall of text. Holding each line back for
 * {@code length * speechPaceMsPerChar} milliseconds makes the exchange read like speech. The raw
 * character count is deliberately naive: punctuation and spaces count too, which is close enough to
 * a human reading rhythm and avoids language-specific rules.
 */
public final class SpeechPace {

    private SpeechPace() {
    }

    /**
     * Pause to wait before showing a line of the given length.
     *
     * @return milliseconds, clamped to the configured floor and ceiling; 0 disables pacing
     */
    public static long pauseFor(String text, ModConfig config) {
        if (text == null || config == null || config.speechPaceMsPerChar <= 0) {
            return 0L;
        }
        long calculated = (long) text.length() * config.speechPaceMsPerChar;
        if (calculated < config.speechPaceMinMs) {
            return config.speechPaceMinMs;
        }
        if (calculated > config.speechPaceMaxMs) {
            return config.speechPaceMaxMs;
        }
        return calculated;
    }

    /** Convenience overload for callers that only have a line and the per-character rate. */
    public static long pauseFor(String text, int msPerChar, int minMs, int maxMs) {
        if (text == null || msPerChar <= 0) {
            return 0L;
        }
        long calculated = (long) text.length() * msPerChar;
        if (calculated < minMs) {
            return minMs;
        }
        return Math.min(calculated, maxMs);
    }
}
