package com.justaivillagers.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Loads and saves {@link ModConfig}. Never throws at the caller: a broken config file is backed up
 * and replaced by defaults, because a mod must not fail to start over a stray comma.
 */
public final class ConfigStore {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path file;
    private final Log log;
    private final AtomicReference<ModConfig> current = new AtomicReference<>(new ModConfig());

    public ConfigStore(Path file, Log log) {
        this.file = file;
        this.log = log;
    }

    public ModConfig get() {
        return current.get();
    }

    public Path file() {
        return file;
    }

    /** Reads the file, creating it with defaults when missing. */
    public ModConfig load() {
        if (file == null) {
            return current.get();
        }
        try {
            if (!Files.exists(file)) {
                ModConfig defaults = new ModConfig();
                save(defaults);
                current.set(defaults);
                return defaults;
            }
            String json = Files.readString(file, StandardCharsets.UTF_8);
            ModConfig parsed = GSON.fromJson(json, ModConfig.class);
            if (parsed == null) {
                parsed = new ModConfig();
            }
            parsed.sanitize();
            current.set(parsed);
            // Gson fills absent fields with the class defaults, so an old config keeps working, but
            // the new options stay invisible until the player happens to know about them. Rewriting
            // the file once teaches the file about every current key.
            if (isMissingAnyKey(json)) {
                save(parsed);
            }
            return parsed;
        } catch (JsonSyntaxException | IOException e) {
            log.error("Config {} is unreadable, falling back to defaults", e, file);
            ModConfig defaults = new ModConfig();
            try {
                Path backup = file.resolveSibling(file.getFileName() + ".broken");
                Files.move(file, backup, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                log.warn("Broken config moved to {}", backup);
            } catch (IOException ignored) {
                // Backing up is best-effort only.
            }
            save(defaults);
            current.set(defaults);
            return defaults;
        }
    }

    /**
     * Keys added after the first release. A config written before one of them existed cannot contain
     * it, so its presence proves whether the file already knows the current option set.
     */
    private static final String[] KEYS_ADDED_LATER = {
            "speechPaceMsPerChar", "speechPaceMinMs", "speechPaceMaxMs", "hurtCooldownMs",
    };

    private static boolean isMissingAnyKey(String json) {
        for (String key : KEYS_ADDED_LATER) {
            if (!json.contains(key)) {
                return true;
            }
        }
        return false;
    }

    public void save(ModConfig config) {
        if (file == null) {
            return;
        }
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(config), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Could not write config {}", e, file);
        }
    }
}
