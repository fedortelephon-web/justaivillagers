package com.justaivillagers.core;

/**
 * Turns a failed backend health check into one short line the player can act on. Without it the mod
 * is silent when the server is missing and an unstarted Ollama looks like a broken mod.
 *
 * <p>Kept to one sentence: the address, the download link, the model command and the mod page
 * together are a wall of grey chat text that players scroll past.
 */
public final class BackendHint {

    public static final String OLLAMA_URL = "https://ollama.com/download";

    /** The command that starts the local server, offered as text for the player to paste. */
    public static final String START_COMMAND = "ollama serve";
    public static final String MODRINTH_URL = "https://modrinth.com/mod/just-ai-villagers";

    private BackendHint() {
    }

    /**
     * A player-facing hint, or {@code null} when the backend is reachable so nothing needs saying.
     *
     * @param health current backend state
     * @param lang   any supported language code, or a raw Minecraft locale
     */
    public static String forHealth(LlmBackend.Health health, String lang, String model, String baseUrl) {
        if (health == null || health.available) {
            return null;
        }
        LangPack pack = LangData.of(Lang.normalise(lang));
        String detail = health.detail == null ? "" : health.detail;
        if (detail.contains("not installed")) {
            return pack.t("model_missing", model, model);
        }
        return pack.t("backend_down", OLLAMA_URL);
    }

    /** Shown once per world join when the backend is down, so the player is not left guessing. */
    public static String unavailableBanner(String lang) {
        return LangData.of(Lang.normalise(lang)).t("banner");
    }

    /** Shorter repeat of the first hint; the player has already seen the full message once. */
    public static String repeatHint(String lang) {
        return LangData.of(Lang.normalise(lang)).t("backend_down.repeat");
    }
}
