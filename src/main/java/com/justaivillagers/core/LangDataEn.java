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
                        Map.entry("model_missing", "Model %s is not downloaded. Open settings with O and press Download."),
                        Map.entry("backend_down", "Ollama is not running. Install and start it: %s - then open settings with O and press Check."),
                        Map.entry("banner", "Open the settings screen with O."),
                        Map.entry("settings.status.nomodel", "Server is up, but model %s is not downloaded"),
                        Map.entry("settings.check.tip", "Test the address and model typed above, without saving"),
                        Map.entry("backend_down.repeat", "Ollama is still not running, or the server address is wrong, so villagers cannot talk. Settings: press O."),
                        Map.entry("settings.server.hint", "server address"),
                        Map.entry("settings.server.tip", "Where Ollama or a compatible server listens. Default http://127.0.0.1:11434"),
                        Map.entry("settings.model.custom.hint", "model name"),
                        Map.entry("settings.model.custom.tip", "Any model already pulled in Ollama. List them with: ollama list"),
                        Map.entry("settings.model.tip", "%s - %s to download, about %s of RAM."),
                        Map.entry("settings.model.get.tip", "Download model %s through Ollama"),
                        Map.entry("settings.ollama.download.tip", "Download the Ollama installer. Run it yourself afterwards - the mod never launches programs."),
                        Map.entry("settings.model.tip.qwen2_5_0_5b", "Fastest and simplest. Fine for short lines, often incoherent."),
                        Map.entry("settings.model.tip.qwen3_0_6b", "A little more coherent than the smallest, keeps the topic well."),
                        Map.entry("settings.model.tip.gemma3_1b", "A good balance on a weak machine: natural speech, little RAM."),
                        Map.entry("settings.model.tip.llama3_2_1b", "Noticeably livelier and chattier, but sometimes wanders off."),
                        Map.entry("settings.model.tip.gemma3_4b", "The best speech quality here. Needs about 5 GB of free RAM."),

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
                        Map.entry("settings.model.get", "Download"),
                        Map.entry("settings.model.installed", "installed"),
                        Map.entry("settings.model.notinstalled", "not downloaded"),
                        Map.entry("settings.model.downloading", "downloading: %s"),
                        Map.entry("settings.model.done", "model ready"),
                        Map.entry("settings.ollama.title", "Ollama is not running"),
                        Map.entry("settings.ollama.body", "The mod needs Ollama to talk. Download it, install it, then press Check again."),
                        Map.entry("settings.ollama.download", "Download Ollama"),
                        Map.entry("settings.help", "Why doesn't it work?.."),
                        Map.entry("settings.help.tip", "Step-by-step guide: installing Ollama, starting the server, downloading a model"),
                        Map.entry("help.page", "Page %s of %s"),
                        Map.entry("help.prev", "Back"),
                        Map.entry("help.next", "Next"),

        Map.entry("help.title", "How to set up talking to villagers"),
        Map.entry("help.copied", "Copied"),
        Map.entry("help.intro", "For villagers to answer you, the mod uses a neural network that runs right on your computer through a program called Ollama. Everything works locally and for free; you need the internet only once, to download the files."),
        Map.entry("help.step1", "## Step 1. Install Ollama\nGo to ollama.com/download and install the program for your system:\n#copy|copy the website link|https://ollama.com/download\n• Windows: download OllamaSetup.exe, run it and wait for the installation to finish.\n• macOS: download the archive, unpack it and drag the Ollama icon into Applications.\n• Linux: open a terminal and run: curl -fsSL https://ollama.com/install.sh | sh\n#copy|copy the Linux download command|curl -fsSL https://ollama.com/install.sh | sh"),
        Map.entry("help.step2", "## Step 2. Start the server\nThe program has to run in the background while you play:\n• Windows: once installed, Ollama starts by itself (a llama icon appears in the bottom right corner, near the clock). If it is not there, find Ollama with Windows search and start it.\n• macOS: open Ollama from Applications.\n• Linux: in a terminal run sudo systemctl start ollama\nFallback: to start the local ollama server, run this command in a terminal of your operating system: ollama serve.\n#copy|copy the command|ollama serve"),
        Map.entry("help.step3", "## Step 3. Download the brains (a language model)\n1. In the game, open the mod settings (the O key by default).\n2. Pick a suitable model from the list. Tip: for a first run and weaker PCs, Qwen2.5 0.5B fits best (only 398 MB and the fastest to answer).\n3. Press Download and wait until the status changes to Saved."),
        Map.entry("help.step4", "## Step 4. Check the connection and start playing\n1. In the same settings menu press the Check button.\n2. If the status says Connected, everything is set up correctly! Close the menu, walk up to any villager and press the talk key.\n(Leave the Server address field at its default: http://127.0.0.1:11434. Only advanced users need to change it, if the neural network runs on another PC)."),
        Map.entry("help.ifnot", "## Common problems and how to fix them\n• The error Not connected\nThe Ollama program is not running or is blocked. Check whether the llama icon is in the tray (near the clock). If it is, but the errors do not go away, make sure your antivirus or Windows firewall is not blocking Ollama.\n• The error Model not downloaded\nThere is a connection to the program, but no neural network is loaded. Go back to Step 3 and press Download.\n• Villagers take too long to think\nYour computer struggles with the chosen neural network. Pick a smaller model in the settings (for example 0.5B or 1.5B) and reduce the maximum answer length."),
                        Map.entry("help.close", "Close"),
                        Map.entry("settings.ollama.copy", "Copy link"),
                        Map.entry("settings.ollama.command.copy", "Copy command"),
                        Map.entry("settings.ollama.cancel", "Cancel download"),
                        Map.entry("settings.ollama.cancel.tip", "Stop downloading the installer"),
                        Map.entry("settings.ollama.copied", "Link copied - paste it into your browser's address bar."),
                        Map.entry("settings.ollama.command.copied", "Command copied - paste it into PowerShell or cmd."),
                        Map.entry("settings.ollama.command.tip", "Copies the command that starts the Ollama server"),
                        Map.entry("settings.ollama.paste.hint", "Paste the copied command into PowerShell or cmd (press Win+R, type cmd, Enter) and press Enter. Then press Check."),
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

        Map.entry("settings.spontaneous.tip", "A villager sometimes starts talking to you on its own when you stand nearby. Turn it off if you do not want unexpected lines."),
        Map.entry("settings.group.tip", "In the evening two villagers may strike up a conversation with each other, and you overhear it. Turn it off if you only want talks with you."),
        Map.entry("settings.reactions.tip", "A villager snaps back briefly when you hit it. Turn it off to make hits met with silence."),
        Map.entry("settings.fallback.tip", "If the model does not answer or the server is unreachable, the villager says a canned line instead of falling silent. Turn it off to keep it quiet on errors."),
        Map.entry("settings.debug.tip", "Writes the request sent and the answer received to the game log. Only needed to hunt bugs; leave it off in normal play."),
        Map.entry("settings.temperature.tip", "How varied the villager's wording is. Low means predictable and to the point, high means livelier but sometimes off the mark."),
        Map.entry("settings.maxTokens.tip", "How much text fits in one answer. Higher lets the villager say more, but you wait longer for the reply."),
        Map.entry("settings.speechPace.tip", "How fast the answer appears in the dialogue window, letter by letter. 0 shows it all at once; higher is slower and livelier."),
        Map.entry("settings.history.tip", "How many recent lines the villager remembers in a conversation. More means better context, but each answer takes longer to compute."),
        Map.entry("settings.key.dialog.tip", "The key that starts a conversation with a villager. Click the row and press the key you want."),
        Map.entry("settings.key.settings.tip", "The key that opens this settings menu. Click the row and press the key you want."),
                        Map.entry("settings.debug", "Log prompts"),
                        Map.entry("settings.timeout", "Request timeout"),
                        Map.entry("settings.history", "Memory length"),
                        Map.entry("settings.key.dialog", "Talk to villager"),
                        Map.entry("settings.key.settings", "Mod settings"),
                        Map.entry("settings.key.press", "press a key..."),
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
