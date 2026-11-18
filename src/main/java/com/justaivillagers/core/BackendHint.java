package com.justaivillagers.core;

/**
 * Turns a failed backend health check into one line the player can act on.
 *
 * <p>Without this the mod is silent when the model server is missing: villagers simply never speak,
 * and the player has no way to tell a broken mod from an unstarted Ollama. The line names the problem
 * and carries the two links a new user actually needs - where to download Ollama, and the mod page.
 */
public final class BackendHint {

    public static final String OLLAMA_URL = "https://ollama.com/download";
    public static final String MODRINTH_URL = "https://modrinth.com/mod/justaivillagers";

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
        return pack.t("backend_down", baseUrl, OLLAMA_URL, model, MODRINTH_URL);
    }

    /** Shown once per world join when the backend is down, so the player is not left guessing. */
    public static String unavailableBanner(String lang) {
        return LangData.of(Lang.normalise(lang)).t("banner");
    }
}
