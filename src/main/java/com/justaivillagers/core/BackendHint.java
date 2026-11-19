package com.justaivillagers.core;

/**
 * Turns a failed backend health check into one short line the player can act on.
 *
 * <p>Without this the mod is silent when the model server is missing: villagers simply never speak,
 * and the player has no way to tell a broken mod from an unstarted Ollama. The line names the
 * problem, gives the one command that fixes it, and points at the settings key.
 *
 * <p>The text is deliberately one sentence. The previous version stacked the address, the download
 * link, the model command and the mod page into a wall of grey chat text, which players scroll past
 * without reading.
 */
public final class BackendHint {

    public static final String OLLAMA_URL = "https://ollama.com/download";

    /** The command that starts the local server, offered as text for the player to paste. */
    public static final String START_COMMAND = "ollama serve";
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
        return pack.t("backend_down", OLLAMA_URL);
    }

    /** Shown once per world join when the backend is down, so the player is not left guessing. */
    public static String unavailableBanner(String lang) {
        return LangData.of(Lang.normalise(lang)).t("banner");
    }

    /**
     * The reminder repeated while the player keeps talking to villagers with no backend.
     *
     * <p>Deliberately shorter than the first message: the player has already seen the full hint, and
     * a wall of text every time they press the key would be worse than silence.
     */
    public static String repeatHint(String lang) {
        return LangData.of(Lang.normalise(lang)).t("backend_down.repeat");
    }
}
