package com.justaivillagers.core;

import java.util.List;

/** One generation request: a system prompt plus a user prompt. */
public final class LlmRequest {

    public final String systemPrompt;
    public final String userPrompt;
    public final double temperature;
    public final int maxTokens;

    public LlmRequest(String systemPrompt, String userPrompt, double temperature, int maxTokens) {
        this.systemPrompt = systemPrompt == null ? "" : systemPrompt;
        this.userPrompt = userPrompt == null ? "" : userPrompt;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }

    /** Convenience factory for tests and simple call sites. */
    public static LlmRequest of(String prompt, int maxTokens) {
        return new LlmRequest("", prompt, 1.0, maxTokens);
    }

    public static LlmRequest of(String prompt) {
        return of(prompt, 96);
    }

    /** Optional pre-built chat history, used by the OpenAI-compatible provider. */
    public List<ChatTurn> history = List.of();

    /**
     * The player's own words when this request answers something they typed. Separate from
     * {@link #userPrompt}, which is the whole assembled block (personality, time, trade, history,
     * question) and is not useful to echo. Empty for unprompted lines and reactions.
     */
    public String playerLine = "";
}
