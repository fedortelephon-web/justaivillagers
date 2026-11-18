package com.justaivillagers.core;

/**
 * Everything the villager knows about the world around it. Filled in by the game layer; the core
 * only ever reads it, which keeps prompts testable outside Minecraft.
 */
public final class WorldContext {

    public enum TimeOfDay {
        MORNING, DAY, EVENING, SUNSET, NIGHT, DAWN
    }

    public enum Weather {
        CLEAR, RAIN, THUNDER
    }

    public final TimeOfDay time;
    public final Weather weather;
    /** Biome category already mapped to a prompt-friendly word ("plains", "desert", ...) or null. */
    public final String biome;
    public final boolean indoors;
    public final String profession;
    public final int tradeLevel;

    public WorldContext(TimeOfDay time, Weather weather, String biome, boolean indoors, String profession,
                        int tradeLevel) {
        this.time = time == null ? TimeOfDay.DAY : time;
        this.weather = weather == null ? Weather.CLEAR : weather;
        this.biome = biome;
        this.indoors = indoors;
        this.profession = profession == null ? "villager" : profession;
        this.tradeLevel = Math.max(1, Math.min(5, tradeLevel));
    }

    public static WorldContext minimal() {
        return new WorldContext(TimeOfDay.DAY, Weather.CLEAR, null, false, "villager", 1);
    }

    /**
     * Converts a raw world time into a phase. Minecraft time 0 is 06:00, 6000 is noon, 12000 is
     * sunset, 13000 is night and 23000 is the start of dawn.
     */
    public static TimeOfDay timeOfDay(long worldTime) {
        long t = worldTime % 24000L;
        if (t < 0) {
            t += 24000L;
        }
        if (t < 2000L) {
            return TimeOfDay.MORNING;
        }
        if (t < 11000L) {
            return TimeOfDay.DAY;
        }
        if (t < 12000L) {
            return TimeOfDay.EVENING;
        }
        if (t < 13000L) {
            return TimeOfDay.SUNSET;
        }
        if (t < 23000L) {
            return TimeOfDay.NIGHT;
        }
        return TimeOfDay.DAWN;
    }

    /** Rough indoor test used by the game layer: sky light below this means "under a roof". */
    public static final int INDOOR_SKY_LIGHT = 12;
}
