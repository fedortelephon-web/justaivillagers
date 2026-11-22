package com.justaivillagers.core;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Builds the prompts.
 *
 * <p>All wording lives in {@link LangPack}, so adding a language never means touching this file. The
 * original had three giant {@code switch} blocks with a Russian and an English copy of every sentence,
 * which is exactly why a third language was unthinkable.
 */
public final class PromptBuilder {

    private PromptBuilder() {
    }

    public static String systemPrompt(String lang) {
        return LangData.of(Lang.normalise(lang)).t("system");
    }

    /** Prompt for a direct answer to something the player said. */
    public static LlmRequest reply(ModConfig config, String lang, VillagerProfile profile, WorldContext ctx,
                                   String playerMessage, DialogueHistory history, String sessionLabel) {
        LangPack pack = LangData.of(Lang.normalise(lang));
        String prompt = base(pack, profile, ctx, history, sessionLabel)
                + pack.t("reply_ask", playerMessage);
        LlmRequest request = new LlmRequest(pack.t("system"), prompt, config.temperature, config.maxTokens);
        request.playerLine = playerMessage;
        return request;
    }

    /** Prompt for a line the villager says to nobody in particular. */
    public static LlmRequest spontaneous(ModConfig config, String lang, VillagerProfile profile, WorldContext ctx,
                                         DialogueHistory history, String sessionLabel) {
        LangPack pack = LangData.of(Lang.normalise(lang));
        // A nudge towards a different subject each time. Without it the same model at the same
        // temperature produced almost the same mutter every few seconds.
        List<String> topics = pack.topics;
        String topic = topics.get(ThreadLocalRandom.current().nextInt(topics.size()));
        String prompt = base(pack, profile, ctx, history, sessionLabel) + pack.t("mutter_ask", topic);
        return new LlmRequest(pack.t("system"), prompt, config.temperature, config.maxTokens);
    }

    /** Prompt used when the player wakes a sleeping villager. */
    public static LlmRequest sleeping(ModConfig config, String lang, VillagerProfile profile, WorldContext ctx,
                                      String playerMessage, DialogueHistory history, String sessionLabel) {
        LangPack pack = LangData.of(Lang.normalise(lang));
        String prompt = base(pack, profile, ctx, history, sessionLabel)
                + pack.t("sleep_situation") + pack.t("sleep_ask");
        LlmRequest request = new LlmRequest(pack.t("system"), prompt, config.temperature, config.maxTokens);
        // Waking a villager is still the player saying something, so the debug echo shows it.
        request.playerLine = playerMessage;
        return request;
    }

    /** Prompt for a villager reacting to an event: being hit, or fleeing a hostile mob. */
    public static LlmRequest reaction(ModConfig config, String lang, VillagerProfile profile, WorldContext ctx,
                                      String eventKey, String mobName, DialogueHistory history, String sessionLabel) {
        LangPack pack = LangData.of(Lang.normalise(lang));
        String situation;
        if ("hurt".equals(eventKey)) {
            situation = pack.t("hurt_situation");
        } else if ("flee".equals(eventKey)) {
            String mob = mobName == null || mobName.isBlank() ? pack.t("monster") : mobName;
            if (profile.child) {
                // A child has a father and no mother, so the cry for help goes to dad.
                situation = pack.t("flee_child_situation", mob);
            } else {
                situation = pack.t("flee_adult_situation", mob, mob);
            }
        } else {
            situation = pack.t("react_generic");
        }
        String prompt = base(pack, profile, ctx, history, sessionLabel) + situation + pack.t("shout_ask");
        return new LlmRequest(pack.t("system"), prompt, config.temperature, config.maxTokens);
    }

    /** Prompt for one line of a two-villager evening conversation. */
    public static LlmRequest groupLine(ModConfig config, String lang, VillagerProfile speaker, WorldContext ctx,
                                       VillagerProfile listener, List<String[]> conversation, String timeLabel) {
        LangPack pack = LangData.of(Lang.normalise(lang));
        StringBuilder transcript = new StringBuilder();
        for (String[] line : conversation) {
            transcript.append(line[0]).append(": ").append(line[1]).append('\n');
        }
        String head = pack.t("group_head", speaker.name, pack.profession(ctx.profession),
                speaker.personality, pack.style(speaker.styleKey()));
        String tail = pack.t("group_tail", listener.name,
                transcript.length() == 0 ? pack.t("group_empty") : transcript.toString());
        return new LlmRequest(pack.t("system"), head + envLine(pack, ctx) + tradeLine(pack, ctx) + tail,
                config.temperatureGroup, config.maxTokens);
    }

    private static String base(LangPack pack, VillagerProfile profile, WorldContext ctx,
                               DialogueHistory history, String sessionLabel) {
        StringBuilder sb = new StringBuilder();
        sb.append(pack.t("name", profile.name));
        if (profile.child) {
            sb.append(pack.t("child_block", profile.personality));
        } else {
            sb.append(pack.t("adult_personality", profile.personality));
            sb.append(pack.t("adult_style", pack.style(profile.styleKey())));
            if (profile.stutters) {
                sb.append(pack.t("stutter"));
            }
        }
        sb.append(envLine(pack, ctx));
        sb.append(tradeLine(pack, ctx));
        if (sessionLabel != null) {
            sb.append("\n\n").append(sessionLabel);
        }
        return sb.toString();
    }

    /**
     * The villager's trade and the things it may mention.
     *
     * <p>This is what stops a leatherworker from offering crocodile leather. The system prompt only
     * says "fit your trade"; the model has no idea what fits unless it is told, so the plausible
     * invention is exactly the kind of detail a person would make up. A list of real items closes
     * the gap without forbidding speech, because a villager who may mention a saddle and a rabbit
     * hide has no reason to reach for something that does not exist.
     */
    private static String tradeLine(LangPack pack, WorldContext ctx) {
        String trade = pack.trade(ctx.profession);
        return trade.isEmpty() ? "" : "\n" + trade;
    }

    private static String envLine(LangPack pack, WorldContext ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append(pack.t("env", pack.time(ctx.time), pack.weather(ctx.weather),
                pack.t(ctx.indoors ? "inside" : "outside"),
                pack.profession(ctx.profession), ctx.tradeLevel));
        if (ctx.biome != null && !ctx.biome.isBlank()) {
            sb.append(pack.t("env_biome", pack.biome(ctx.biome)));
        }
        return sb.toString();
    }
}
