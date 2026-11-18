package com.justaivillagers.fabric;

import com.justaivillagers.core.WorldContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;

/** Reads a villager's situation out of the live game state. */
public final class VillagerContextReader {

    private VillagerContextReader() {
    }

    public static WorldContext read(VillagerEntity villager) {
        World world = villager.getEntityWorld();
        long time = world.getTimeOfDay();
        WorldContext.TimeOfDay timeOfDay = WorldContext.timeOfDay(time);

        WorldContext.Weather weather;
        if (world.isThundering()) {
            weather = WorldContext.Weather.THUNDER;
        } else if (world.isRaining()) {
            weather = WorldContext.Weather.RAIN;
        } else {
            weather = WorldContext.Weather.CLEAR;
        }

        BlockPos pos = villager.getBlockPos();
        boolean indoors = world.getLightLevel(net.minecraft.world.LightType.SKY, pos) < WorldContext.INDOOR_SKY_LIGHT;

        VillagerData data = villager.getVillagerData();
        String profession = professionName(data);
        int level = data == null ? 1 : data.level();

        return new WorldContext(timeOfDay, weather, biomeName(world, pos), indoors, profession, level);
    }

    private static String professionName(VillagerData data) {
        if (data == null) {
            return "villager";
        }
        RegistryEntry<?> entry = data.profession();
        String id = entry.getIdAsString();
        int slash = id.indexOf(':');
        return slash >= 0 ? id.substring(slash + 1) : id;
    }

    private static String biomeName(World world, BlockPos pos) {
        try {
            return world.getBiome(pos).getKey()
                    .map(key -> {
                        String path = key.getValue().getPath();
                        return path.replace('_', ' ');
                    })
                    .orElse(null);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static boolean isVillager(Entity entity) {
        return entity instanceof VillagerEntity;
    }

    public static boolean isChild(VillagerEntity villager) {
        return villager.isBaby();
    }

    public static boolean isSleeping(Entity entity) {
        return entity instanceof LivingEntity living && living.isSleeping();
    }
}
