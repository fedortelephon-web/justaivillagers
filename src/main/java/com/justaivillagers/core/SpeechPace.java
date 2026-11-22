package com.justaivillagers.core;

/**
 * Converts a line of text into a pause proportional to its spoken length.
 *
 * <p>Each line is held back for {@code length * speechPaceMsPerChar} milliseconds so a dialogue
 * arrives at reading speed instead of all at once. The character count includes punctuation and
 * spaces, which is close enough to speech rhythm and needs no per-language rules.
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
