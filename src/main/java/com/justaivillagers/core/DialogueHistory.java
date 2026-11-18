package com.justaivillagers.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-villager conversation memory.
 *
 * <p>Fixes three problems of the original: the {@code HashMap} was written from both the game thread
 * and an async worker (a real corruption risk), the history grew unbounded within its TTL, and NPC
 * state was never evicted for unloaded villagers.
 */
public final class DialogueHistory {

    private final Map<UUID, Deque<ChatTurn>> byVillager = new ConcurrentHashMap<>();
    private final int maxMessages;
    private final long ttlMillis;

    public DialogueHistory(int maxMessages, int ttlSeconds) {
        this.maxMessages = Math.max(2, maxMessages);
        this.ttlMillis = Math.max(10, ttlSeconds) * 1000L;
    }

    public void add(UUID villager, ChatTurn.Role role, String text) {
        if (villager == null || text == null || text.isBlank()) {
            return;
        }
        Deque<ChatTurn> turns = byVillager.computeIfAbsent(villager, k -> new ArrayDeque<>());
        synchronized (turns) {
            turns.addLast(new ChatTurn(role, text));
            while (turns.size() > maxMessages) {
                turns.removeFirst();
            }
        }
    }

    /** Live turns only, oldest first. */
    public List<ChatTurn> get(UUID villager) {
        List<ChatTurn> out = new ArrayList<>();
        Deque<ChatTurn> turns = byVillager.get(villager);
        if (turns == null) {
            return out;
        }
        long cutoff = System.currentTimeMillis() - ttlMillis;
        synchronized (turns) {
            for (ChatTurn turn : turns) {
                if (turn.createdAtMillis >= cutoff) {
                    out.add(turn);
                }
            }
        }
        return out;
    }

    public boolean isEmpty(UUID villager) {
        return get(villager).isEmpty();
    }

    /** Rendered history for prompt injection, trimmed to the most recent turns. */
    public String format(UUID villager, int maxTurns, String userLabel, String villagerLabel) {
        List<ChatTurn> turns = get(villager);
        if (turns.isEmpty()) {
            return "";
        }
        int from = Math.max(0, turns.size() - Math.max(1, maxTurns));
        StringBuilder sb = new StringBuilder();
        for (int i = from; i < turns.size(); i++) {
            ChatTurn turn = turns.get(i);
            sb.append(turn.role == ChatTurn.Role.USER ? userLabel : villagerLabel)
                    .append(": ")
                    .append(turn.text)
                    .append('\n');
        }
        return sb.toString().trim();
    }

    public void clear(UUID villager) {
        byVillager.remove(villager);
    }

    public void clearAll() {
        byVillager.clear();
    }

    /** Drops expired entries; call once in a while (world join, or every N ticks). */
    public void prune() {
        long cutoff = System.currentTimeMillis() - ttlMillis;
        byVillager.entrySet().removeIf(entry -> {
            Deque<ChatTurn> turns = entry.getValue();
            synchronized (turns) {
                turns.removeIf(turn -> turn.createdAtMillis < cutoff);
                return turns.isEmpty();
            }
        });
    }

    public int size() {
        return byVillager.size();
    }
}
