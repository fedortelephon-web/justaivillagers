package com.justaivillagers.fabric;

import java.util.ArrayDeque;
import java.util.Deque;

import com.justaivillagers.core.ModConfig;
import com.justaivillagers.core.SpeechPace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

/**
 * Delivers villager lines to chat at a human speaking rate.
 *
 * <p>Generation is far faster than speech: several villagers answering the same question, or a
 * handful of spontaneous lines, would otherwise land on the same tick as an unreadable wall of
 * text. Each line is queued with a due time computed by {@link SpeechPace}; the queue is pumped
 * once per client tick and releases a line only when it is due.
 */
public final class SpeechQueue {

    /** One queued chat line. */
    private static final class Entry {
        final String name;
        final String text;
        final long dueNanos;

        Entry(String name, String text, long dueNanos) {
            this.name = name;
            this.text = text;
            this.dueNanos = dueNanos;
        }
    }

    private final Deque<Entry> pending = new ArrayDeque<>();

    /** When the last line was shown, so two lines never appear on the same tick. */
    private long lastShownNanos;

    public void clear() {
        pending.clear();
        lastShownNanos = 0L;
    }

    public int size() {
        return pending.size();
    }

    /**
     * Queues a line. {@code name} may be null for lines that already carry their own speaker prefix
     * (the group dialogue format).
     */
    public void enqueue(String name, String text, ModConfig config) {
        if (text == null || text.isBlank()) {
            return;
        }
        long now = System.nanoTime();
        long base = Math.max(now, lastShownNanos);
        long due = base + SpeechPace.pauseFor(text, config) * 1_000_000L;
        pending.addLast(new Entry(name, text, due));
    }

    /**
     * Queues a reaction: no speaking delay, and placed ahead of anything already waiting.
     *
     * <p>Being hit is an event, not a conversation. Pacing it by length made the villager answer
     * seconds after the punch, and letting it sit behind an evening dialogue made it worse, so it
     * jumps the queue and is shown on the next tick.
     */
    public void enqueueReaction(String name, String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        pending.addFirst(new Entry(name, text, System.nanoTime()));
    }

    /**
     * Queues a line that was already paced upstream, so it is shown as soon as the previous line has
     * had its turn. Used by the group dialogue, which sleeps between its own lines.
     */
    public void enqueueImmediate(String name, String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        pending.addLast(new Entry(name, text, Math.max(System.nanoTime(), lastShownNanos)));
    }

    /** Called every client tick. Sends at most one due line, so output is naturally staggered. */
    public void tick(MinecraftClient client) {
        if (pending.isEmpty() || client.player == null) {
            return;
        }
        Entry head = pending.peekFirst();
        long now = System.nanoTime();
        if (head.dueNanos > now) {
            return;
        }
        pending.pollFirst();
        lastShownNanos = now;
        client.player.sendMessage(Text.literal(format(head)), false);
    }

    private static String format(Entry entry) {
        return entry.name == null ? entry.text : "§e[" + entry.name + "] §f" + entry.text;
    }
}
