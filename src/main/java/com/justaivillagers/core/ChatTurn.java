package com.justaivillagers.core;

/** A single chat message exchanged with a villager. */
public final class ChatTurn {

    public enum Role {
        USER,
        ASSISTANT;

        public String wireName() {
            return this == USER ? "user" : "assistant";
        }
    }

    public final Role role;
    public final String text;
    public final long createdAtMillis;

    public ChatTurn(Role role, String text) {
        this(role, text, System.currentTimeMillis());
    }

    public ChatTurn(Role role, String text, long createdAtMillis) {
        this.role = role;
        this.text = text;
        this.createdAtMillis = createdAtMillis;
    }
}
