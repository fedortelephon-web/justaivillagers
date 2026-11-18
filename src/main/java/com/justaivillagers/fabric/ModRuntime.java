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
        history = new DialogueHistory(config.maxHistoryMessages, config.historyTtlSeconds);
        profiles = new VillagerProfile.Cache();
        service = new VillagerAiService(config, LOGGER, backend, history, profiles);
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
