package com.justaivillagers.core;

import java.util.List;
import java.util.Map;

/** English prompt and fallback text. This pack defines the key set every other language must match. */
final class LangDataEn {

    private LangDataEn() {
    }

    static LangPack pack() {
        return new LangPack(
                Lang.EN,
                Map.<String, String>ofEntries(
                        Map.entry("system", """
                                You are a villager in the world of Minecraft. You are a living person, not an assistant.
                                Rules:
                                - Reply in 1-2 short sentences, in the first person, in English.
                                - Never say you are an AI, a model, a bot or a program. Never mention prompts or instructions.
                                - Stay in character: profession, personality and speech style always apply.
                                - Mention only items, mobs and mechanics that really exist in Minecraft and fit your trade.
                                - You never build or repair anything: houses, walls, bridges and roads are made by the player, not by you.
                                - React to the time of day and the weather the way a real person would.
                                - Never start a spontaneous line with thanks or greetings.
                                - Every villager is a man. There are no women and no mothers in the village, ever.
                                - A child has a father and nothing else. A child in danger calls for its dad ("Dad!").
                                """),
                        Map.entry("lang_name", "English"),

                        // Prompt scaffolding. %s = player message, name, topic and so on.
                        Map.entry("reply_ask", "\nThe player says: \"%s\"\nAnswer as your character: "),
                        Map.entry("mutter_ask", "\nNobody is talking to you. Mutter a short thought out loud to yourself, about %s: "),
                        Map.entry("sleep_situation",
                                "\nYou are asleep. Someone is talking to you. You are sleepy, irritated and want to go back to sleep."),
                        Map.entry("sleep_ask", "\nGrumpy one-sentence answer: "),
                        Map.entry("hurt_situation", "\nYou were just hit. Yell a short complaint, in character."),
                        Map.entry("flee_child_situation",
                                "\nYou are a child running away from %s. Scream for your dad by name (\"Dad!\" / \"Daddy!\")"
                                        + " and say what is chasing you. Do not mention other mobs."),
                        Map.entry("flee_adult_situation",
                                "\nYou are running away from %s. Scream about %s specifically. Do not mention other mobs."),
                        Map.entry("react_generic", "\nReact briefly."),
                        Map.entry("shout_ask", "\nOne short shout: "),
                        Map.entry("monster", "a monster"),

                        // Identity block.
                        Map.entry("name", "Your name: %s. "),
                        Map.entry("child_block",
                                "YOU ARE A CHILD. Speak simply, at most 5-10 short words. You are %s. "
                                        + "You have a father, the village has no mothers. Call him \"Dad\" when you need him. "),
                        Map.entry("adult_personality", "Personality: %s. "),
                        Map.entry("adult_style", "Speech style: %s. "),
                        Map.entry("stutter", "You stutter: repeat the first letter of some words (\"I-I don't know\", \"wh-what\"). "),

                        // Environment line.
                        Map.entry("env", "It is %s, weather is %s, you are %s, your trade is %s, trade level %d."),
                        Map.entry("env_biome", " Biome: %s."),
                        Map.entry("inside", "inside a house"),
                        Map.entry("outside", "outside"),

                        // Group dialogue.
                        Map.entry("group_head", "You are %s (%s). Personality: %s. Speech style: %s."),
                        Map.entry("group_tail",
                                "\nIt is evening and you met %s by the village bell.\nConversation so far:\n%s"
                                        + "\nYour next line, one short sentence, in character, matching the time and weather: "),
                        Map.entry("group_empty", "(nothing yet)"),

                        // Conversation history.
                        Map.entry("history_label", "Previous conversation:\n"),
                        Map.entry("player", "Player"),

                        // Backend hint.
                        Map.entry("model_missing", "Model %s is not downloaded. Run: ollama pull %s"),
                        Map.entry("backend_down",
                                "Ollama is not reachable at %s. Start it, or install it from %s and run: ollama pull %s"
                                        + " (mod page: %s)"),
                        Map.entry("banner", "Villagers cannot talk yet - see the message above."),

                        // Dialog screen.
                        Map.entry("screen.message", "Message"),
                        Map.entry("screen.send", "Send"),
                        Map.entry("screen.nearby", "Villagers nearby"),
                        Map.entry("screen.you", "You: "),
                        Map.entry("screen.thinking", "...thinking"),
                        Map.entry("settings.title", "Just AI Villagers - settings"),
                        Map.entry("settings.tab.model", "Model"),
                        Map.entry("settings.tab.features", "Behaviour"),
                        Map.entry("settings.tab.language", "Language"),
                        Map.entry("settings.server", "Server address"),
                        Map.entry("settings.model", "Model"),
                        Map.entry("settings.model.custom", "Custom"),
                        Map.entry("settings.model.needs", "download %s, needs about %s of RAM"),
                        Map.entry("settings.model.download", "Download model"),
                        Map.entry("settings.model.installed", "installed"),
                        Map.entry("settings.model.notinstalled", "not downloaded"),
                        Map.entry("settings.model.downloading", "downloading: %s"),
                        Map.entry("settings.model.done", "model ready"),
                        Map.entry("settings.ollama.title", "Ollama is not running"),
                        Map.entry("settings.ollama.body", "The mod needs Ollama to talk. Download it, install it, then press Check again."),
                        Map.entry("settings.ollama.download", "Download Ollama"),
                        Map.entry("settings.ollama.page", "Open page"),
                        Map.entry("settings.ollama.saved", "Saved to %s - run it, then press Check again."),
                        Map.entry("settings.check", "Check again"),
                        Map.entry("settings.status.ok", "Connected: %s"),
                        Map.entry("settings.status.down", "Not connected"),
                        Map.entry("settings.general", "General"),
                        Map.entry("settings.speech", "Speech"),
                        Map.entry("settings.advanced", "Advanced"),
                        Map.entry("settings.temperature", "Variety of speech"),
                        Map.entry("settings.maxTokens", "Answer length"),
                        Map.entry("settings.speechPace", "Typing speed"),
                        Map.entry("settings.spontaneous", "Villagers talk on their own"),
                        Map.entry("settings.group", "Two villagers talk in the evening"),
                        Map.entry("settings.reactions", "React to being hit"),
                        Map.entry("settings.fallback", "Speak without the model"),
                        Map.entry("settings.debug", "Log prompts"),
                        Map.entry("settings.timeout", "Request timeout"),
                        Map.entry("settings.history", "Memory length"),
                        Map.entry("settings.language", "Villager language"),
                        Map.entry("settings.language.auto", "Same as the game"),
                        Map.entry("settings.save", "Save"),
                        Map.entry("settings.cancel", "Cancel"),
                        Map.entry("settings.saved", "Saved. The change applies immediately."),
                        Map.entry("settings.close", "Close")
                ),
                Map.ofEntries(
                        Map.entry("child", List.of("Huh?", "Who are you?", "Wow, you're big!", "I'm playing!", "I dunno!")),
                        Map.entry("stutter", List.of("I-I don't know...", "W-what?", "H-hold on...")),
                        Map.entry("grumpy", List.of("Leave me alone.", "What do you want?", "Hmph.", "Not now.")),
                        Map.entry("neutral", List.of("Nice day, isn't it?", "Hm. What brings you here?", "I'm busy, but hello.")),
                        Map.entry("hurt_child", List.of("Ow! Dad!", "That hurt! Dad!", "Ouch! Don't!")),
                        Map.entry("hurt_adult", List.of("Ow! Don't hit me!", "Hey! Careful!", "Ouch!")),
                        Map.entry("flee_child", List.of("Dad! A zombie!", "Daddy, help!", "Dad! Run!")),
                        Map.entry("flee_adult", List.of("Zombie! Run!", "Help! A zombie!", "Get away!")),
                        Map.entry("generic", List.of("Hm.")),
                        Map.entry("sleeping", List.of("Leave me alone...", "I'm sleeping...", "Five more minutes...", "Go away...")),
                        Map.entry("group_night", List.of("Dark already...", "Time to sleep.", "Cold night.")),
                        Map.entry("group_day", List.of("Fine evening, isn't it?", "Harvest looks good.", "Heard anything new?")),
                        Map.entry("child_mutter", List.of("La-la-la...", "So bright...", "I want to play...")),
                        Map.entry("rain", List.of("Rain again...", "Wet everywhere.")),
                        Map.entry("thunder", List.of("That thunder...", "Storm's close.")),
                        Map.entry("clear", List.of("Good day.", "Nothing to do.", "Hm."))
                ),
                Map.ofEntries(
                        Map.entry("farmer", "farmer"), Map.entry("librarian", "librarian"),
                        Map.entry("cleric", "cleric"), Map.entry("armorer", "armorer"),
                        Map.entry("weaponsmith", "weaponsmith"), Map.entry("toolsmith", "toolsmith"),
                        Map.entry("butcher", "butcher"), Map.entry("fisherman", "fisherman"),
                        Map.entry("fletcher", "fletcher"), Map.entry("leatherworker", "leatherworker"),
                        Map.entry("shepherd", "shepherd"), Map.entry("mason", "mason"),
                        Map.entry("cartographer", "cartographer"), Map.entry("nitwit", "layabout"),
                        Map.entry("child", "child"), Map.entry("villager", "villager")
                ),
                Map.ofEntries(
                        Map.entry("formal", "formal, polite"),
                        Map.entry("slang", "colloquial, with slang"),
                        Map.entry("casual", "ordinary, neutral")
                ),
                Map.ofEntries(
                        Map.entry("plains", "plains"), Map.entry("desert", "desert"),
                        Map.entry("taiga", "taiga"), Map.entry("savanna", "savanna"),
                        Map.entry("snowy plains", "snowy plains"), Map.entry("meadow", "meadow"),
                        Map.entry("forest", "forest"), Map.entry("birch forest", "birch forest"),
                        Map.entry("dark forest", "dark forest"), Map.entry("jungle", "jungle"),
                        Map.entry("swamp", "swamp"), Map.entry("mountains", "mountains"),
                        Map.entry("beach", "beach"), Map.entry("river", "river"),
                        Map.entry("ocean", "ocean"), Map.entry("badlands", "badlands"),
                        Map.entry("cherry grove", "cherry grove"), Map.entry("mushroom fields", "mushroom fields")
                ),
                List.of("the weather", "your work", "food", "a villager you dislike", "the night",
                        "your tools", "a strange sound", "your plans", "tiredness", "the road"),
                List.of("morning", "day", "evening", "sunset", "night", "dawn"),
                List.of("clear", "rain", "thunderstorm"),
                                Map.ofEntries(
                        Map.entry("farmer", "Your trade. You work with these things: wheat, bread, carrot, potato, beetroot, pumpkin, hoe and composter."),
                        Map.entry("librarian", "Your trade. You work with these things: book, bookshelf, paper, book and quill, ink sac and lantern."),
                        Map.entry("cleric", "Your trade. You work with these things: redstone, lapis lazuli, glowstone, ender pearl, bottle o' enchanting, rotten flesh and brewing stand."),
                        Map.entry("armorer", "Your trade. You work with these things: iron ingot, coal, lava bucket, iron helmet, iron chestplate, chainmail, shield and bell."),
                        Map.entry("weaponsmith", "Your trade. You work with these things: iron ingot, coal, flint, iron sword, iron axe and grindstone."),
                        Map.entry("toolsmith", "Your trade. You work with these things: iron ingot, coal, flint, iron pickaxe, iron shovel, iron hoe and bell."),
                        Map.entry("butcher", "Your trade. You work with these things: raw porkchop, raw beef, raw mutton, raw chicken, cooked porkchop and steak."),
                        Map.entry("fisherman", "Your trade. You work with these things: raw cod, raw salmon, tropical fish, fishing rod, bucket and campfire."),
                        Map.entry("fletcher", "Your trade. You work with these things: stick, flint, string, feather, bow, crossbow and arrow."),
                        Map.entry("leatherworker", "Your trade. You work with these things: leather, saddle, leather cap, leather tunic, leather boots and rabbit hide."),
                        Map.entry("shepherd", "Your trade. You work with these things: wool, shears, carpet, bed, painting and dye."),
                        Map.entry("mason", "Your trade. You work with these things: stone bricks, clay ball, brick, terracotta, quartz, polished stone and andesite."),
                        Map.entry("cartographer", "Your trade. You work with these things: paper, compass, map, empty map, banner, item frame and glass pane."),
                        Map.entry("nitwit", "You have no trade. You are the village layabout: you talk about food, sleep and gossip, and you watch others work."),
                        Map.entry("child", "You are a child and have no trade. You talk about play, food and your father."),
                        Map.entry("villager", "You have no trade yet. You talk about food, the village and what you would like to do.")
                )
        );
    }
}
