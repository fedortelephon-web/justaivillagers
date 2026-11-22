package com.justaivillagers.core;

/**
 * User-editable settings, written to {@code config/justaivillagers.json} on first launch and
 * re-read on every world join: endpoint, model, temperature, token limit, keep-alive, timeouts,
 * history size and the feature toggles.
 */
public final class ModConfig {

    /** "ollama" (native /api/generate) or "openai" (any /v1/chat/completions compatible server). */
    public String provider = "ollama";

    /** Base URL without a trailing slash. */
    public String baseUrl = "http://127.0.0.1:11434";

    public String model = "gemma3:4b";

    /** Only used by the "openai" provider. */
    public String apiKey = "";

    /** HTTP timeout for a generation request. A stuck model must never freeze the mod. */
    public int requestTimeoutSeconds = 30;

    /** Timeout for the cheap health ping. */
    public int healthTimeoutSeconds = 3;

    /** Re-check the backend at most this often, so starting Ollama mid-session is noticed. */
    public int healthRefreshSeconds = 20;

    public double temperature = 0.8;

    public double temperatureGroup = 0.9;

    /** num_predict. Stops the model from rambling and cuts latency a lot. */
    public int maxTokens = 160;

    /** Ollama keep_alive, so the model is not evicted between conversations. */
    public String keepAlive = "30m";

    /** How long a villager remembers the conversation. */
    public int historyTtlSeconds = 900;

    /** Hard cap on remembered messages; the prompt is trimmed to this. */
    public int maxHistoryMessages = 12;

    /**
     * Max simultaneous generation requests. Extra requests are dropped, not queued forever.
     *
     * <p>Kept at 5 so a group question to the maximum five nearby villagers is delivered in full;
     * anything lower silently drops part of the group.
     */
    public int maxConcurrentRequests = 5;

    /** Chance per villager per tick to mutter something. Original value kept for parity. */
    public double spontaneousChance = 2.0e-4;

    /**
     * Milliseconds of pause per typed character. The model answers faster than a person talks, so
     * lines are held back for {@code length * speechPaceMsPerChar} to sound like speech.
     */
    public int speechPaceMsPerChar = 90;

    /** Floor and ceiling for the computed pause, so one word is not instant and a paragraph is finite. */
    public int speechPaceMinMs = 600;
    public int speechPaceMaxMs = 12000;

    public boolean enableSpontaneousTalk = true;

    public boolean enableGroupDialogues = true;

    /** Delay between lines of a two-villager evening dialogue. */
    public int groupDialogueDelayMs = 3000;

    public boolean enableEventReactions = true;

    /**
     * How long a villager stays quiet after reacting to a hit, in milliseconds. Without it one
     * request goes out per punch and a player can outrun a local LLM. Only gates hit reactions;
     * talking is never blocked.
     */
    public int hurtCooldownMs = 4000;

    /** Use a canned line instead of silence when the model is unavailable. */
    public boolean enableFallbackLines = true;

    /** "", "en" or "ru". Empty means "follow the game language". */
    public String languageOverride = "";

    /** Print the raw prompt and response to the log. Handy for prompt work, noisy otherwise. */
    public boolean debugPrompts = false;

    public ModConfig copy() {
        ModConfig c = new ModConfig();
        c.provider = provider;
        c.baseUrl = baseUrl;
        c.model = model;
        c.apiKey = apiKey;
        c.requestTimeoutSeconds = requestTimeoutSeconds;
        c.healthTimeoutSeconds = healthTimeoutSeconds;
        c.healthRefreshSeconds = healthRefreshSeconds;
        c.temperature = temperature;
        c.temperatureGroup = temperatureGroup;
        c.maxTokens = maxTokens;
        c.keepAlive = keepAlive;
        c.historyTtlSeconds = historyTtlSeconds;
        c.maxHistoryMessages = maxHistoryMessages;
        c.maxConcurrentRequests = maxConcurrentRequests;
        c.spontaneousChance = spontaneousChance;
        c.speechPaceMsPerChar = speechPaceMsPerChar;
        c.speechPaceMinMs = speechPaceMinMs;
        c.speechPaceMaxMs = speechPaceMaxMs;
        c.enableSpontaneousTalk = enableSpontaneousTalk;
        c.enableGroupDialogues = enableGroupDialogues;
        c.groupDialogueDelayMs = groupDialogueDelayMs;
        c.enableEventReactions = enableEventReactions;
        c.hurtCooldownMs = hurtCooldownMs;
        c.enableFallbackLines = enableFallbackLines;
        c.languageOverride = languageOverride;
        c.debugPrompts = debugPrompts;
        return c;
    }

    /** Clamps obviously broken values instead of letting them crash the mod later. */
    public void sanitize() {
        if (provider == null || !(provider.equals("ollama") || provider.equals("openai"))) {
            provider = "ollama";
        }
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "http://127.0.0.1:11434";
        }
        while (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if (model == null || model.isBlank()) {
            model = "gemma3:4b";
        }
        requestTimeoutSeconds = clamp(requestTimeoutSeconds, 5, 300);
        healthTimeoutSeconds = clamp(healthTimeoutSeconds, 1, 30);
        healthRefreshSeconds = clamp(healthRefreshSeconds, 5, 600);
        temperature = clamp(temperature, 0.0, 2.0);
        temperatureGroup = clamp(temperatureGroup, 0.0, 2.0);
        maxTokens = clamp(maxTokens, 16, 512);
        historyTtlSeconds = clamp(historyTtlSeconds, 10, 3600);
        maxHistoryMessages = clamp(maxHistoryMessages, 2, 40);
        maxConcurrentRequests = clamp(maxConcurrentRequests, 1, 8);
        groupDialogueDelayMs = clamp(groupDialogueDelayMs, 500, 15000);
        hurtCooldownMs = clamp(hurtCooldownMs, 0, 60000);
        spontaneousChance = Math.max(0.0, Math.min(1.0, spontaneousChance));
        speechPaceMsPerChar = clamp(speechPaceMsPerChar, 0, 300);
        speechPaceMinMs = clamp(speechPaceMinMs, 0, 10000);
        speechPaceMaxMs = clamp(speechPaceMaxMs, speechPaceMinMs, 60000);
        if (keepAlive == null || keepAlive.isBlank()) {
            keepAlive = "30m";
        }
        if (languageOverride == null) {
            languageOverride = "";
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
