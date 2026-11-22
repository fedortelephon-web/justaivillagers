package com.justaivillagers.fabric;

import java.nio.file.Files;
import java.nio.file.Path;

import com.justaivillagers.core.ConfigStore;
import com.justaivillagers.core.DialogueHistory;
import com.justaivillagers.core.LlmBackend;
import com.justaivillagers.core.Log;
import com.justaivillagers.core.ModConfig;
import com.justaivillagers.core.OllamaBackend;
import com.justaivillagers.core.OpenAiBackend;
import com.justaivillagers.core.VillagerAiService;
import com.justaivillagers.core.VillagerProfile;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Owns the long-lived objects: config, backend and the service. Rebuilt when the config file
 * changes and torn down on world exit, so nothing leaks between worlds.
 */
public final class ModRuntime {

    /** Single logger for the whole mod. */
    static final Log LOGGER = new Slf4jLog("justaivillagers");

    private static ModRuntime instance;

    private final ConfigStore store;
    private ModConfig config;
    private LlmBackend backend;
    private VillagerProfile.Cache profiles;
    private DialogueHistory history;
    private VillagerAiService service;
    private long configStamp;

    private ModRuntime(Path configFile) {
        this.store = new ConfigStore(configFile, LOGGER);
        this.config = store.load();
        this.configStamp = stamp(configFile);
        rebuild();
    }

    public static synchronized ModRuntime get() {
        if (instance == null) {
            Path file = FabricLoader.getInstance().getConfigDir().resolve("justaivillagers.json");
            instance = new ModRuntime(file);
        }
        return instance;
    }

    private static long stamp(Path file) {
        try {
            return Files.getLastModifiedTime(file).toMillis();
        } catch (Exception e) {
            return 0L;
        }
    }

    private void rebuild() {
        if (service != null) {
            service.shutdown();
        }
        config.sanitize();
        backend = "openai".equals(config.provider)
                ? new OpenAiBackend(config, LOGGER)
                : new OllamaBackend(config, LOGGER);
        // Keep the existing profiles and history across a settings change: regenerating them here
        // would rename every villager in the world and forget the conversation so far, which looks
        // like the mod reset itself. They are only recreated on a cold start.
        if (history == null) {
            history = new DialogueHistory(config.maxHistoryMessages, config.historyTtlSeconds);
        }
        if (profiles == null) {
            profiles = new VillagerProfile.Cache();
        }
        service = new VillagerAiService(config, LOGGER, backend, history, profiles);
    }

    /**
     * Applies settings edited in the GUI: writes the file, swaps in the new config and rebuilds the
     * backend so the change takes effect without a restart.
     */
    public void apply(ModConfig updated) {
        updated.sanitize();
        store.save(updated);
        this.config = updated;
        this.configStamp = stamp(store.file());
        rebuild();
        LOGGER.info("Settings saved to {}", store.file());
    }

    /** Writes the current config back to disk, for the "reset to defaults" path. */
    public void persist() {
        store.save(config);
        this.configStamp = stamp(store.file());
    }

    /** Path of the config file, shown in the settings screen. */
    public Path configFile() {
        return store.file();
    }

    /** Drops the backend health cache so the next check actually talks to the server. */
    public void recheckBackend() {
        if (backend instanceof OllamaBackend) {
            ((OllamaBackend) backend).health(true);
        } else {
            backend.health();
        }
    }

    /** Fresh backend state for the settings screen, bypassing the cache. */
    public LlmBackend.Health healthNow() {
        if (backend instanceof OllamaBackend) {
            return ((OllamaBackend) backend).health(true);
        }
        return backend.health();
    }

    /** Re-reads the config file if it changed on disk. Cheap enough to call on world join. */
    public void refreshIfChanged() {
        long now = stamp(store.file());
        if (now != 0L && now != configStamp) {
            configStamp = now;
            config = store.load();
            rebuild();
            LOGGER.info("Reloaded config from {}", store.file());
        }
    }

    public ModConfig config() {
        return config;
    }

    /** Timestamp of the config file as last read or written; used to tell "reloaded" from "same". */
    long configStamp() {
        return configStamp;
    }

    public VillagerAiService service() {
        return service;
    }

    public VillagerProfile.Cache profiles() {
        return profiles;
    }

    public void onWorldLeft() {
        service.clearAll();
    }

    public void shutdown() {
        service.shutdown();
    }
}
