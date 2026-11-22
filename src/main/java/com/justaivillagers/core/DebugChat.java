package com.justaivillagers.core;

import java.util.function.Consumer;

/**
 * A place for the core to hand short debug lines to whatever can show them to the player.
 *
 * <p>The core has no idea what chat is - it is plain Java and runs under tests - so it exposes a
 * sink that the Fabric layer fills in. Without a sink (tests, or the title screen) lines go nowhere,
 * which is exactly what should happen.
 */
public final class DebugChat {

    /** Longest line worth putting in chat; beyond this a chat message is unreadable anyway. */
    private static final int MAX_LINE = 240;

    /**
     * One shouted line reaches every nearby villager as its own request, so the same text arrives
     * here several times in a row. Echoing each one filled the chat with copies of a single
     * question, so a repeat of the last line within this window is dropped.
     */
    private static final long REPEAT_WINDOW_MS = 5000L;

    private static volatile Consumer<String> sink;
    private static volatile String lastText = "";
    private static volatile long lastAt = 0L;

    private DebugChat() {
    }

    /** Installs the receiver, or {@code null} to throw debug lines away. */
    public static void setSink(Consumer<String> newSink) {
        sink = newSink;
    }

    /** True when someone is listening, so callers can skip formatting entirely. */
    public static boolean active() {
        return sink != null;
    }

    /** Offers one line to the sink; never throws, because a debugging aid must not break dialogue. */
    public static void emit(String text) {
        Consumer<String> target = sink;
        if (target == null || text == null || text.isBlank()) {
            return;
        }
        String oneLine = text.replace('\n', ' ').replace('\r', ' ').trim();
        if (oneLine.length() > MAX_LINE) {
            oneLine = oneLine.substring(0, MAX_LINE - 1) + "\u2026";
        }
        synchronized (DebugChat.class) {
            long now = System.currentTimeMillis();
            if (oneLine.equals(lastText) && now - lastAt < REPEAT_WINDOW_MS) {
                lastAt = now;
                return;
            }
            lastText = oneLine;
            lastAt = now;
        }
        try {
            target.accept(oneLine);
        } catch (RuntimeException e) {
            // A chat line is never worth an exception in the middle of a conversation.
        }
    }
}
