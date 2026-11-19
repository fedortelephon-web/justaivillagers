# Changelog

## Unreleased

### Fixed

- **Picking a model fills the name field.** Selecting a catalogue model left the old name in the
  editable field, because a rebuild deliberately preserves whatever was typed; the picker now
  writes the chosen id in as well.
- **The Cancel button appears the moment a download starts.** It was only added on the next
  rebuild, which happened on the next unrelated click, so an unwanted download looked
  uncontrollable.
- **The repeat reminder names the other real cause.** It said Ollama was not running; a wrong
  server address produces the same silence, and it now says so.

### Changed

- **Chat links copy instead of opening.** Minecraft's open action hands the URL to whatever handler
  the system registered, which is not always a browser, and its confirmation dialog then leads
  nowhere. Clicking a link now copies it, and a chat line says where to paste it.
- **"Open page" is gone, replaced by two copy buttons.** One copies `https://ollama.com/download`,
  the other copies `ollama serve`, and a short line under them explains that the command goes into
  PowerShell or cmd (Win+R, `cmd`, Enter). A button that opens nothing is worse than a string the
  player can paste anywhere.

## 1.2.0 - a settings screen, and Ollama without a terminal

This is the Minecraft 1.20.1 build of the same release: the feature set is identical to the
1.21.11 and 1.21.1 jars, compiled for Java 17 as 1.20.1 expects.


### Fixed

- **Check now tests what is on screen.** It used to probe the *saved* config, so changing the
  address or picking a model and pressing Check re-tested the old values and looked broken. It now
  probes the address and model currently typed, without saving, and its tooltip says so.
- **The status line no longer lies.** It printed whichever model happened to be selected, so it
  could claim "connected" for a model that was never checked. It now names the model that was
  actually probed, and distinguishes "server up, model not downloaded" from "server unreachable".
- **"Open page" opens a browser.** It called `java.awt.Desktop`, which does nothing in a game JVM
  (the AWT toolkit is not initialised), so the button appeared dead. It now uses Minecraft's own
  opener with the platform command as a fallback, and logs the outcome.
- **The status text no longer overlaps the progress bar**, and is indented to line up with the
  buttons instead of starting a few pixels to their left.
- **Language names were mojibake.** The list showed `EspaГ±ol`, `FranГ§ais` and `Р сГСГСГ...`
  because the strings had been through a CP1251 round trip. All nine names are correct again, and
  the same corruption in the Russian and Portuguese setting labels was repaired.
- **Unknown progress no longer looks frozen.** Some Ollama steps ("removing unused layers") report
  no total, so the bar sat at 0%. It now sweeps, and the caption gets animated dots.

### Changed

- **The "backend down" message is one line.** It was a paragraph carrying the address, the download
  link, the `ollama pull` command and the mod page, which players scroll past. It now names the
  problem, gives the download link and points at the settings key. URLs in chat are clickable.
- **The mod repeats the reminder when the backend is still down.** Talking to villagers with no
  Ollama used to fail silently after the first message had scrolled away; pressing the talk key now
  repeats a short reminder at most once every 45 seconds.
- **Buttons have tooltips.** Model rows describe what each model is like to talk to, the text fields
  explain themselves, and the download and Ollama buttons say what they do.
- **Text fields show a grey placeholder** ("server address", "model name") when empty, and are a
  couple of pixels narrower than the buttons so their outlines line up.

### Added


- **An in-game settings screen.** Press `O` to open it. Every option in
  `config/justaivillagers.json` is reachable: server address, model, temperature, answer length,
  typing speed, memory length, the four feature toggles and the villager language.
  Temperature, answer length, typing speed and memory length are sliders you drag; the screen
  also shows the mod's two keybinds and lets you rebind them by clicking the row and pressing a
  key. The layout adapts to the window, so it fits small GUI scales instead of spilling over the
  Save button.

  The keybind works while playing, not from the title screen: Minecraft does not deliver keybinds to
  a screen that is already open, and a config button on the mod list would need ModMenu as an extra
  dependency. The screen is still reachable before the first villager speaks, because pressing `O`
  in a new world costs nothing.

  Settings are edited on a copy and written only on Save, so Cancel really cancels. Saving rebuilds
  the backend in place — no restart, and the villagers keep their names and the conversation so far
  instead of looking like the mod reset itself.

- **A model picker with five small models.** `qwen2.5:0.5b` (398 MB), `qwen3:0.6b` (523 MB),
  `gemma3:1b` (806 MB), `llama3.2:1b` (1.3 GB) and `gemma3:4b` (3.4 GB). Each row shows the real
  download size and the RAM it needs, taken from the Ollama library rather than guessed, because
  "about a few gigabytes" is not enough to decide with.

- **Model downloads from inside the mod.** The Download button on any model row pulls it through
  Ollama's own API and shows a progress bar. This is the step where most people give up: being told
  to run `ollama pull gemma3:4b` assumes you know what a terminal is.

- **Ollama installer download.** When the server is unreachable the screen offers to download the
  installer and shows where it was saved. It deliberately does not run it — a Minecraft mod that
  silently launches executables is exactly what malware does, and Windows would show a SmartScreen
  prompt the player did not ask for. The player double-clicks the file; it is one extra step and no
  surprises.

- A "Check again" button re-probes the server, so starting Ollama mid-session is noticed without
  reopening the screen.

### Changed

- The settings screen and its 41 new strings are translated into all nine languages, and the keybind
  label is translated in all eleven Minecraft language files.

### Fixed

- Rebuilding the runtime after a settings change used to recreate the profile cache and the dialogue
  history, which renamed every villager and forgot the current conversation. They now survive a
  settings change and are only created on a cold start.

## 1.1.5 - villagers know their trade

### Added

- **A trade block for every profession, in all nine languages.** Each villager is now told what its
  trade actually handles: a leatherworker gets leather, a saddle, leather armour and rabbit hide; a
  mason gets stone bricks, clay, terracotta and quartz. Villagers with no trade — the layabout, the
  child, the unemployed villager — get their own block instead of an empty one.

  This closes the "crocodile leather" problem properly. The prompt used to say only "mention things
  that fit your trade", which leaves the model to guess what fits, and the guess is exactly where
  invented materials came from. Listing the real items removes the need to invent.

- The block reaches every kind of line: direct answers, spontaneous mutters, reactions and the
  evening two-villager conversations.

### Fixed

- Two tests guard the data: one fails if any profession in any language has no trade block, another
  fails if the block is present in the pack but never makes it into the prompt.

## 1.1.4 - villagers do not build

### Fixed

- **Villagers no longer talk about building or repairing things.** The model used to invent
  construction work — "we should strengthen the walls", "I need to finish my roof" — which villagers
  in Minecraft never do. Every language pack now states plainly that houses, walls, bridges and roads
  are made by the player, not by the villager.

  The rule is phrased as a statement of the villager's role rather than as a ban. A prompt that says
  "never mention building" makes the model mention building more often, because the word itself puts
  the subject in context. Saying what the villager does not do, and who does it instead, avoids that.

- A test now fails if any language pack is missing the rule, so a future translation cannot drop it
  silently.

## 1.1.3 - the languages players actually use

### Added

- **Turkish, Polish and Italian.** Nine languages now: English, Turkish, Spanish, Portuguese,
  German, Polish, French, Italian and Russian.
- Turkish is the big one. A 2026 server-market survey put it at **9.7% of Minecraft players** —
  second after English and seven times French, which the mod already spoke. Guessing the order
  without data had put the wrong languages in first.

### Fixed

- The order of `Lang.ALL` now follows measured player share rather than a guess, and the class
  comment records the numbers so the next language is not chosen by intuition either.

## 1.1.2 - more languages

### Added

- **Spanish, Portuguese, German and French.** Villagers now speak the language the game is set to
  instead of only English and Russian. Regional variants such as `es_mx`, `pt_br` and `pt_pt` are
  covered, and anything else falls back to English.
- Translations are complete rather than cosmetic: names, surnames, character traits, canned fallback
  lines, the dialog screen and the prompts steering the model are all localised per language.
- Language packs are data (`core/LangDataXx.java`), so the next language is one file plus one
  registry entry, with a test that fails if any key is missing.

### Fixed

- A German, French, Spanish or Portuguese client used to get **English** villagers even with the mod
  installed, because only two languages existed and everything non-Russian fell through to English.


## 1.1.1 - polish

### Added

- A chat hint on world join when the model backend cannot be reached. It names the configured
  endpoint, the download link for Ollama and the mod page on Modrinth, so a first-time player is not
  left guessing why nobody talks.
- Villagers have surnames now (about one in four), which makes two villagers sharing a name rare.
- The backend state is written to the log once per join, so a bug report shows the endpoint and
  model that were actually in use.

### Fixed

- **A non-English, non-Russian game language produced Russian villagers.** Only the English locale
  was mapped to English and everything else fell through to Russian. German, French, Spanish and the
  rest now get English.
- **One villager repeated the stutter example from the prompt** as the opening of every line
  ("К-как д-дела?"). The example is now neutral and the model writes its own words.
- **Reactions to a hit or a monster replayed the previous conversation** instead of shouting, which
  made villagers answer an old question while being chased.
- **The model prefixed its own name** ("Стёпа: ..."), so chat showed the name twice.
- **Villagers answered their own mutterings** ("you're still saying that gibberish") and repeated one
  phrasing forever. Spontaneous lines no longer carry the transcript and now pick a topic.
- **The whole village spoke at once on world join.** There is a short grace period after loading and
  a shared limit of one unprompted line at a time.
- Name collisions: the first name pool was small enough that ten villagers collided 30% of the time.

### Changed

- Defaults: `temperature` 1.2 to 0.8, `temperatureGroup` 1.3 to 0.9, `historyTtlSeconds` 120 to 900,
  `requestTimeoutSeconds` 60 to 30, `maxTokens` 96 to 160. Existing config files keep their values.

## 1.1.0 — rewrite

A ground-up rework of the internals. Behaviour is close to 1.0.0, but the bugs are gone and the mod
is now configurable.

### Fixed

- **Hitting a villager produced two identical reactions.** In single player the integrated server
  runs in the same JVM, so Fabric fired the attack callback from both the client and the server path.
  The callback now ignores the server side, and a short per-villager debounce catches any remaining
  duplicate.
- **All dialogue appeared in one instant.** The model answers much faster than a person talks, so an
  evening exchange landed as a wall of text. Lines are now paced at `length × 90 ms` (clamped to
  0.6–12 s) and released one per tick through a chat queue. Reactions to a punch or a monster skip
  the pace entirely and jump the queue, because an event should answer at once.
- **The mod crashed on startup on 1.21.11.** `Identifier.of(String, String)` was left in its named
  form during remapping, so the client died with `NoSuchMethodError` before the main menu. Only a
  real launch showed it; the unit tests never touch Minecraft classes.
- **Dialog screen closed after every message.** It now stays open so a conversation can continue.
- **Enter was detected by polling GLFW every frame.** It is handled through the screen's key
  handler, so keyboard layouts and IME work.
- **Hitting a villager was guessed** from `getAttackCooldownProgress() > 0.8`, which fired on every
  fully charged swing near a villager, including misses. It is now an exact attack event.
- **Data races.** The dialog history `HashMap` was written from the game thread and from
  `CompletableFuture.runAsync` at the same time; the name/personality/stutter maps were unsynchronised.
  History is now thread-safe, bounded, and expires by TTL; the profile cache is bounded and locked.
- **Requests could hang forever.** The HTTP client had a connect timeout but no per-request
  timeout. Every request now has one, plus a bounded thread pool and a concurrency cap.
- **The group-dialogue flag could stay set forever**, silently disabling group dialogues for the
  rest of the session. It is now released in a guaranteed path.
- **Backend availability was cached permanently**, so starting Ollama after Minecraft required a
  restart. Health is refreshed periodically.
- **`Thread.sleep(3000)` inside an async task** is replaced by an interruptible pause.
- **Two O(villagers × entities) scans per tick** collapsed into one, with a smaller radius.
- **JSON was parsed with a hand-written regex and an unescaper** that mangled escapes. Gson is used
  instead, with a proper fallback chain.
- **Every language other than English was treated as Russian.** Language resolution is explicit.
- **Empty `catch (Exception) {}` blocks** swallowed errors silently; failures are logged and, where
  the player can act on it, surfaced as a message.
- **Metadata** now matches reality: correct icon path and a real icon, the declared license matches
  the repository, no leftover `examplemod`/`modid` assets, no empty mixin list pointing at mixin
  configs that no longer exist.

### Added

- **Group dialogue.** Pressing the dialog key while not aiming at a villager addresses every
  villager within 12 blocks, closest first, capped at five.
- **Dialog input is focused immediately**, so typing works without clicking the field first.
- Config file with validation and clamping: provider, base URL, model, API key, timeouts, token cap,
  temperature, concurrency, feature toggles, language override, prompt debugging.
- OpenAI-compatible backend alongside Ollama.
- Fallback lines, so villagers still say something when the model server is down.
- Sanitizer that strips markdown, quotes, role prefixes and runaway length from model output.
- Pure-Java core with a dependency-free test suite and a fake HTTP server covering success, server
  down, missing model, HTTP 500, timeout, malformed JSON, quoted output and markdown output.

### Changed

- **Every villager is a man.** Female names are gone from both name pools, and the system prompt
  states that the village has no women and no mothers.
- **Children have a father only.** A child in danger calls for its dad ("Папа!") instead of help in
  general, in both the prompt and the fallback lines.
- **Hitting a villager no longer floods the model.** Each punch used to fire its own request, so a
  player could spam hits faster than a local LLM could answer. A villager now answers at most once per
  `hurtCooldownMs` (4 s by default); talking to it is never blocked by that cooldown.
- **A villager keeps its name when it grows up.** Names used to be drawn from an age-dependent pool,
  so a baby that became an adult was renamed while its conversation history stayed behind under the
  same UUID, and the next prompt signed that history with a stranger's name. Names and personalities
  now come from pools that are valid at any age, and the cached profile keeps its identity when only
  the age changes.
- **Old config files are upgraded in place**, so new options such as the speech pace appear in the
  file instead of staying invisible defaults.
- **Enter now sends and closes the screen**; the answers are delivered to chat, so the world is
  reachable again immediately.
- **Concurrency default raised to 5**, so a question to a full group of five villagers is not partly
  dropped.
- Prompts moved out of giant `switch` blocks into a dedicated builder with per-personality text.
- Request payloads now set `num_predict` and `keep_alive`.
- Default version bumped to 1.1.0.
- **License is MIT.**

## 1.1.0 for Minecraft 1.21.1

The same release built against Minecraft 1.21.1, Fabric Loader 0.16.14 and Fabric API 0.115.0.
Only the Fabric layer differs: 1.21.1 has no `KeyBinding.Category`, uses `keyPressed(int, int, int)`
instead of the `KeyInput` record, exposes `Entity.getPos()` rather than `getEntityPos()`, and takes
`VillagerData.getLevel()` / `getProfession().id()` instead of the record accessors. The pure-Java
core is shared unchanged.
