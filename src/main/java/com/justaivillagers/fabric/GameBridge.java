package com.justaivillagers.fabric;

import com.justaivillagers.core.Lang;
import net.minecraft.client.MinecraftClient;

/** Small helpers that bridge game state into the pure core. */
public final class GameBridge {

    private GameBridge() {
    }

    /**
     * Active language, honouring the config override.
     *
     * <p>Returns one of {@link com.justaivillagers.core.Lang#ALL}; any language the mod does not
     * speak becomes English. Treating every non-English locale as Russian meant a player who set the
     * game to German, French or Spanish got Russian villagers.
     */
    public static String language(String override) {
        if (override != null && !override.isBlank()) {
            return Lang.normalise(override);
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.options != null && client.options.language != null) {
            return Lang.normalise(client.options.language);
        }
        return Lang.EN;
    }
}
