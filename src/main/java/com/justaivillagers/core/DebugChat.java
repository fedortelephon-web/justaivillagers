package com.justaivillagers.core;

import java.util.function.Consumer;

/**
 * Hand-off point for short debug lines from the core to the player. The core is plain Java and does
 * not know about chat, so the Fabric layer installs a sink; with no sink (tests, title screen) the
 * lines are discarded.
 */
public final class DebugChat {

    /** Longest line worth putting in chat; beyond this a chat message is unreadable anyway. */
    private static final int MAX_LINE = 240;

    /** One shout becomes one request per nearby villager; identical lines inside this window are dropped. */
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

    /** Offers one line to the sink. Swallows runtime failures: debugging must not break dialogue. */
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
