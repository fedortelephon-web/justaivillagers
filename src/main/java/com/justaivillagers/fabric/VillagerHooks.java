package com.justaivillagers.fabric;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.VillagerEntity;

/** Thin static bridge so hooks can be called from anywhere without holding a watcher reference. */
public final class VillagerHooks {

    private static VillagerWatcher watcher;

    private VillagerHooks() {
    }

    static void bind(VillagerWatcher instance) {
        watcher = instance;
    }

    public static void onVillagerHurt(MinecraftClient client, VillagerEntity villager) {
        VillagerWatcher current = watcher;
        if (current != null) {
            current.onVillagerHurt(client, villager);
        }
    }
}
