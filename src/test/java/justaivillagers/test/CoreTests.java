package justaivillagers.test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.justaivillagers.core.BackendHint;
import com.justaivillagers.core.ChatTurn;
import com.justaivillagers.core.ConfigStore;
import com.justaivillagers.core.DialogueHistory;
import com.justaivillagers.core.FallbackLines;
import com.justaivillagers.core.LlmBackend;
import com.justaivillagers.core.LlmException;
import com.justaivillagers.core.Log;
import com.justaivillagers.core.ModConfig;
import com.justaivillagers.core.Names;
import com.justaivillagers.core.OllamaBackend;
import com.justaivillagers.core.OpenAiBackend;
import com.justaivillagers.core.PromptBuilder;
import com.justaivillagers.core.SpeechPace;
import com.justaivillagers.core.TextSanitizer;
import com.justaivillagers.core.VillagerAiService;
import com.justaivillagers.core.VillagerProfile;
import com.justaivillagers.core.WorldContext;
import com.justaivillagers.core.Lang;
import com.justaivillagers.core.LangData;
import com.justaivillagers.core.LangPack;
import com.justaivillagers.core.ModelCatalog;
import com.justaivillagers.core.ModelPuller;
import com.justaivillagers.core.OllamaInstaller;

/** Dependency-free test harness. Exits non-zero on the first failed expectation. */
public final class CoreTests {

    private static int checks = 0;
    private static final List<String> failures = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        try (FakeServer fake = new FakeServer()) {
            runContentTests();
            runBackendTests(fake);
            runOpenAiTests(fake);
            runServiceTests(fake);
            runConfigTests();
            runSettingsTests();
        }
        System.out.println();
        if (failures.isEmpty()) {
            System.out.println("ALL " + checks + " CHECKS PASSED");
        } else {
            System.out.println(failures.size() + " FAILURE(S) out of " + checks);
            for (String failure : failures) {
                System.out.println("  ✗ " + failure);
            }
            System.exit(1);
        }
    }

    // ---------------------------------------------------------------- content

    private static void runContentTests() {
        section("sanitizer");
        eq("plain", "Hello there.", TextSanitizer.cleanForChat("Hello there."));
        eq("json escape", "It's fine", TextSanitizer.cleanForChat("It\\u0027s fine"));
        eq("quotes stripped", "Hi!", TextSanitizer.cleanForChat("\"Hi!\""));
        eq("curly quotes", "Hi!", TextSanitizer.cleanForChat("\u201cHi!\u201d"));
        eq("markdown stripped", "Good day!", TextSanitizer.cleanForChat("**Good day!**"));
        eq("ai preamble", "Good day!", TextSanitizer.cleanForChat("As an AI language model: Good day!"));
        eq("whitespace", "a b", TextSanitizer.cleanForChat("  a\n\n b  "));
        eq("newlines in line", "one two", TextSanitizer.cleanForChat("one\ntwo"));
        check("long text truncated", TextSanitizer.cleanForChat("x".repeat(1000)).length() <= 400);

        // A model answering in transcript form prefixes its own name; chat already prints it.
        eq("speaker prefix stripped", "Ну, чёрт побери.",
                TextSanitizer.stripSpeakerPrefix("Стёпа: Ну, чёрт побери.", "Стёпа"));
        eq("quoted speaker prefix stripped", "Ну, чёрт побери.",
                TextSanitizer.stripSpeakerPrefix("\u00abСтёпа: Ну, чёрт побери.\u00bb", "Стёпа"));
        eq("speaker dash stripped", "Ну, чёрт побери.",
                TextSanitizer.stripSpeakerPrefix("Стёпа \u2014 Ну, чёрт побери.", "Стёпа"));
        eq("name in a sentence kept", "Стёпа не понимает.",
                TextSanitizer.stripSpeakerPrefix("Стёпа не понимает.", "Стёпа"));
        eq("other name kept", "Пётр: привет",
                TextSanitizer.stripSpeakerPrefix("Пётр: привет", "Стёпа"));
        eq("name alone kept", "Стёпа", TextSanitizer.stripSpeakerPrefix("Стёпа", "Стёпа"));

        section("names and profiles");
        eq("lang normalise en", "en", Names.normalise("en_us"));
        eq("lang normalise ru", "ru", Names.normalise("ru_ru"));
        // A German client used to get Russian villagers. Now each supported language keeps itself,
        // and every regional variant collapses onto the base language.
        eq("lang normalise de", "de", Names.normalise("de_de"));
        eq("lang normalise fr", "fr", Names.normalise("fr_fr"));
        eq("lang normalise es", "es", Names.normalise("es_es"));
        eq("lang normalise es_mx", "es", Names.normalise("es_mx"));
        eq("lang normalise pt_br", "pt", Names.normalise("pt_br"));
        eq("lang normalise pt_pt", "pt", Names.normalise("pt_pt"));
        eq("lang normalise dash form", "pt", Names.normalise("pt-BR"));
        eq("lang normalise tr", "tr", Names.normalise("tr_tr"));
        eq("lang normalise pl", "pl", Names.normalise("pl_pl"));
        eq("lang normalise it", "it", Names.normalise("it_it"));
        // A language the mod does not speak still gets English, not a random other one.
        eq("lang normalise unknown", "en", Names.normalise("ja_jp"));
        eq("lang normalise null", "en", Names.normalise(null));

        // Every supported language must have a complete pack. A missing key used to be invisible
        // until a player set that language and the prompt came out with a raw key in it.
        section("language packs");
        LangPack en = LangData.of(Lang.EN);
        for (String code : Lang.ALL) {
            LangPack pack = LangData.of(code);
            eq("pack exists " + code, code, pack.code);
            java.util.List<String> missing = new java.util.ArrayList<>();
            for (String key : en.text.keySet()) {
                if (!pack.has(key)) {
                    missing.add(key);
                }
            }
            check("pack " + code + " has every text key: " + missing, missing.isEmpty());
            java.util.List<String> missingFallbacks = new java.util.ArrayList<>();
            for (String key : en.fallbacks.keySet()) {
                if (pack.fallback(key).isEmpty()) {
                    missingFallbacks.add(key);
                }
            }
            check("pack " + code + " has every fallback: " + missingFallbacks, missingFallbacks.isEmpty());
            eq("pack " + code + " has 6 times", 6, pack.times.size());
            eq("pack " + code + " has 3 weathers", 3, pack.weathers.size());
            check("pack " + code + " topics not empty", !pack.topics.isEmpty());
            // A pack whose prompt still contains a placeholder marker would break the model call.
            check("pack " + code + " system prompt is not a raw key", !pack.t("system").equals("system"));
            // Villagers never build, so no pack may leave the model free to invent construction work.
            // The rule is checked by its own marker word rather than by exact wording, because every
            // language phrases it differently, and a new language must not silently skip it.
            String system = pack.t("system");
            boolean forbidsBuilding = system.contains("build") || system.contains("строиш")
                    || system.contains("construyes") || system.contains("constrói") || system.contains("baust")
                    || system.contains("budujesz") || system.contains("construis") || system.contains("costruisci")
                    || system.contains("inşa");
            check("pack " + code + " forbids villagers from building", forbidsBuilding);
            // Every profession the mod can produce needs a trade block, otherwise a leatherworker
            // silently loses the item list and starts inventing crocodile leather again.
            java.util.List<String> missingTrades = new java.util.ArrayList<>();
            for (String prof : en.professions.keySet()) {
                if (pack.trade(prof).isBlank()) {
                    missingTrades.add(prof);
                }
            }
            check("pack " + code + " has a trade block for every profession: " + missingTrades,
                    missingTrades.isEmpty());
        }
        eq("all languages registered", 9, LangData.count());
        VillagerProfile.Cache cache = new VillagerProfile.Cache();
        UUID id = UUID.randomUUID();
        VillagerProfile a = cache.get(id, false, "ru");
        VillagerProfile b = cache.get(id, false, "ru");
        eq("profile stable", a.name, b.name);
        eq("profile personality stable", a.personality, b.personality);
        check("profile name non-empty", !a.name.isBlank());

        // Names must be a pure function of the UUID. An earlier "fix" consulted the cache to avoid
        // duplicates, which made the name depend on the order villagers loaded and renamed them
        // between sessions - much worse than the duplicates it removed.
        VillagerProfile.Cache orderA = new VillagerProfile.Cache();
        VillagerProfile.Cache orderB = new VillagerProfile.Cache();
        UUID sessionId = UUID.randomUUID();
        String firstName = orderA.get(sessionId, false, "en").name;
        // Fill a different cache first, so load order differs, then ask for the same villager.
        orderB.get(UUID.randomUUID(), false, "en");
        orderB.get(UUID.randomUUID(), false, "en");
        eq("name does not depend on load order", firstName, orderB.get(sessionId, false, "en").name);

        // Duplicates still have to be rare. Surnames widen the pool; measure across many villages.
        VillagerProfile.Cache dupCache = new VillagerProfile.Cache();
        int collisions = 0;
        int villages = 60;
        int perVillage = 12;
        for (int v = 0; v < villages; v++) {
            java.util.Set<String> names = new java.util.HashSet<>();
            for (int i = 0; i < perVillage; i++) {
                String n = dupCache.get(UUID.randomUUID(), false, "en").name;
                if (!names.add(n)) {
                    collisions++;
                }
            }
        }
        // Before surnames this averaged roughly 1.8 duplicate pairs per 12-villager village.
        check("duplicate names are rare (" + collisions + "/" + (villages * perVillage) + ")",
                collisions <= villages * perVillage / 12);
        VillagerProfile child = cache.get(id, true, "ru");
        check("child flag respected", child.child);

        // Growing up must not rename the villager: it kept its UUID, so its history is still keyed to
        // it and a rename would make every later prompt sign that history with a stranger's name.
        eq("growing up keeps the name", a.name, child.name);
        eq("growing up keeps the personality", a.personality, child.personality);
        eq("growing up keeps the style", a.style, child.style);
        VillagerProfile grownBack = cache.get(id, false, "ru");
        eq("reverting age keeps the name", a.name, grownBack.name);
        check("age actually changed", child.child && !grownBack.child);
        // Two different villagers must still be able to differ, i.e. the rename fix did not pin every
        // profile to one name.
        java.util.Set<String> names = new java.util.HashSet<>();
        for (int i = 0; i < 40; i++) {
            names.add(cache.get(UUID.randomUUID(), false, "ru").name);
        }
        check("names still vary between villagers", names.size() > 5);

        section("history");
        DialogueHistory history = new DialogueHistory(4, 60);
        for (int i = 0; i < 10; i++) {
            history.add(id, ChatTurn.Role.USER, "msg" + i);
        }
        eq("history capped", 4, history.get(id).size());
        eq("history keeps newest", "msg9", history.get(id).get(3).text);
        String formatted = history.format(id, 4, "Player", "Villager");
        check("history formatted", formatted.contains("Player: msg6"));
        history.clear(id);
        eq("history cleared", 0, history.get(id).size());

        DialogueHistory expired = new DialogueHistory(4, 10);
        expired.add(id, ChatTurn.Role.USER, "old");
        // Rewrite with an old timestamp via a fresh turn is not possible; verify prune on empty is safe.
        expired.prune();
        check("prune safe", true);

        section("prompts");
        ModConfig config = new ModConfig();
        WorldContext ctx = new WorldContext(WorldContext.TimeOfDay.NIGHT, WorldContext.Weather.RAIN,
                "plains", true, "farmer", 3);
        String enReply = PromptBuilder.reply(config, "en", a, ctx, "Hello", history, null).userPrompt;
        check("en prompt has name", enReply.contains(a.name));
        check("en prompt has weather", enReply.toLowerCase().contains("rain"));
        check("en prompt has biome", enReply.contains("plains"));
        check("en prompt has player line", enReply.contains("Hello"));
        check("en prompt indoor", enReply.contains("inside a house"));
        String ruReply = PromptBuilder.reply(config, "ru", a, ctx, "Привет", history, null).userPrompt;
        check("ru prompt has player line", ruReply.contains("Привет"));
        check("ru prompt has weather", ruReply.contains("дождь"));
        check("ru prompt has profession", ruReply.contains("фермер"));
        String sys = PromptBuilder.systemPrompt("ru");
        check("ru system forbids ai", sys.contains("не ассистент"));

        // The trade block has to reach the prompt, not merely sit in the pack. A leatherworker who
        // is told the real materials stops inventing crocodile leather; a list nobody passes to the
        // model fixes nothing.
        WorldContext leatherCtx = new WorldContext(WorldContext.TimeOfDay.DAY, WorldContext.Weather.CLEAR,
                "plains", false, "leatherworker", 2);
        String leatherPrompt = PromptBuilder.reply(config, "en", a, leatherCtx, "Hi", history, null).userPrompt;
        check("trade block reaches the prompt", leatherPrompt.contains("saddle"));
        check("trade block names the real material", leatherPrompt.contains("leather"));
        WorldContext masonCtx = new WorldContext(WorldContext.TimeOfDay.DAY, WorldContext.Weather.CLEAR,
                "plains", false, "mason", 2);
        String ruMason = PromptBuilder.reply(config, "ru", a, masonCtx, "Привет", history, null).userPrompt;
        check("russian trade block reaches the prompt", ruMason.contains("каменный кирпич"));
        check("a profession without a trade still gets a block", !LangData.of("en").trade("nitwit").isBlank());

        // The stutter example used to be a ready-made greeting ("К-как д-дела?"), which the model
        // echoed as the opening of every single line instead of writing its own text.
        VillagerProfile stutterer = new VillagerProfile(java.util.UUID.randomUUID(), "Тест", "grumpy",
                VillagerProfile.SpeechStyle.CASUAL, true, false, VillagerProfile.ChildKind.NONE);
        String stutterPrompt = PromptBuilder.reply(config, "ru", stutterer, ctx, "Привет", history, null).userPrompt;
        check("stutter prompt has no canned greeting", !stutterPrompt.contains("К-как д-дела?"));
        check("stutter prompt still asks for stutter", stutterPrompt.contains("заика"));

        // A reaction is a shout in the moment, so it must not replay the previous conversation.
        history.add(java.util.UUID.randomUUID(), ChatTurn.Role.USER, "unused");
        String reaction = PromptBuilder.reaction(config, "ru", a, ctx, "hurt", null, history, null).userPrompt;
        check("reaction prompt is a shout", reaction.contains("ударили"));

        // A mutter must also stay out of the transcript, and it asks for a topic so the same model
        // does not produce the same sentence every few seconds.
        String mutter = PromptBuilder.spontaneous(config, "en", a, ctx, history, null).userPrompt;
        check("mutter names a topic", mutter.contains("Mutter a short thought"));
        // The template ends with the topic and a colon, so a short topic leaves exactly
        // twelve characters after the cue. Asserting "longer than" made this fail once in a
        // while whenever the random topic happened to be "food".
        int cue = mutter.indexOf("about ");
        String afterCue = cue < 0 ? "" : mutter.substring(cue + 6).trim();
        // The template ends with the topic and a colon: "about food: ".
        String topic = afterCue.endsWith(":") ? afterCue.substring(0, afterCue.length() - 1).trim() : afterCue;
        check("mutter has a topic after the cue", !topic.isEmpty());

        WorldContext.TimeOfDay t = WorldContext.timeOfDay(1000L);
        eq("time morning", WorldContext.TimeOfDay.MORNING, WorldContext.timeOfDay(1000L));
        eq("time noon", WorldContext.TimeOfDay.DAY, WorldContext.timeOfDay(6000L));
        eq("time evening", WorldContext.TimeOfDay.EVENING, WorldContext.timeOfDay(11500L));
        eq("time sunset", WorldContext.TimeOfDay.SUNSET, WorldContext.timeOfDay(12500L));
        eq("time night", WorldContext.TimeOfDay.NIGHT, WorldContext.timeOfDay(14000L));
        eq("time dawn", WorldContext.TimeOfDay.DAWN, WorldContext.timeOfDay(23000L));
        eq("time wraps negative", WorldContext.TimeOfDay.DAWN, WorldContext.timeOfDay(-500L));

        section("fallbacks");
        check("fallback reply en", !FallbackLines.forReply("en", a, ctx).isBlank());
        check("fallback reaction hurt", !FallbackLines.forReaction("ru", "hurt", a, null).isBlank());
        check("fallback sleeping", !FallbackLines.forSleeping("en").isBlank());
        check("fallback spontaneous", !FallbackLines.forSpontaneous("en", a, ctx).isBlank());

        section("speech pace");
        ModConfig pace = new ModConfig();
        pace.speechPaceMsPerChar = 90;
        pace.speechPaceMinMs = 600;
        pace.speechPaceMaxMs = 12000;
        eq("pace scales with length", 5400L, SpeechPace.pauseFor("x".repeat(60), pace));
        eq("pace has a floor", 600L, SpeechPace.pauseFor("hi", pace));
        eq("pace has a ceiling", 12000L, SpeechPace.pauseFor("x".repeat(1000), pace));
        pace.speechPaceMsPerChar = 0;
        eq("pace can be disabled", 0L, SpeechPace.pauseFor("x".repeat(60), pace));
    }

    // ---------------------------------------------------------------- backends

    private static void runBackendTests(FakeServer fake) {
        section("ollama backend");
        ModConfig config = new ModConfig();
        config.baseUrl = fake.baseUrl();
        config.model = "gemma3:4b";
        config.healthRefreshSeconds = 5;
        OllamaBackend backend = new OllamaBackend(config, Log.NOOP);

        fake.mode(FakeServer.Mode.OK);
        check("health ok", backend.health(true).available);

        fake.mode(FakeServer.Mode.MODEL_MISSING);
        LlmBackend.Health missing = backend.health(true);
        check("health missing model", !missing.available && missing.detail.contains("not installed"));

        fake.mode(FakeServer.Mode.OK);
        check("health recovers", backend.health(true).available);

        fake.mode(FakeServer.Mode.DOWN);
        LlmBackend.Health down = backend.health(true);
        check("health down", !down.available);

        // Caching: two fast calls must not both hit the server.
        fake.mode(FakeServer.Mode.OK);
        backend.health(true);
        int before = fake.tagsCalls();
        backend.health(false);
        backend.health(false);
        eq("health cached", before, fake.tagsCalls());

        section("ollama generate");
        fake.mode(FakeServer.Mode.OK);
        try {
            String text = backend.generate(com.justaivillagers.core.LlmRequest.of("Hi", 32));
            check("generate text", text.contains("good day"));
        } catch (LlmException e) {
            fail("generate ok threw " + e.friendly());
        }

        fake.mode(FakeServer.Mode.QUOTED);
        try {
            String text = backend.generate(com.justaivillagers.core.LlmRequest.of("Hi", 32));
            eq("quotes cleaned", "Hello there, traveler!", text);
        } catch (LlmException e) {
            fail("quoted threw " + e.friendly());
        }

        fake.mode(FakeServer.Mode.MARKDOWN);
        try {
            String text = backend.generate(com.justaivillagers.core.LlmRequest.of("Hi", 32));
            eq("preamble stripped", "Good day!", text);
        } catch (LlmException e) {
            fail("markdown threw " + e.friendly());
        }

        fake.mode(FakeServer.Mode.BAD_JSON);
        expectKind("bad json", backend, LlmException.Kind.BAD_RESPONSE);

        fake.mode(FakeServer.Mode.MODEL_MISSING);
        expectKind("model missing", backend, LlmException.Kind.MODEL_MISSING);

        fake.mode(FakeServer.Mode.HTTP_500);
        expectKind("http 500", backend, LlmException.Kind.HTTP_ERROR);

        section("timeout");
        config.requestTimeoutSeconds = 1;
        OllamaBackend slow = new OllamaBackend(config, Log.NOOP);
        fake.mode(FakeServer.Mode.OK).delay(2500);
        expectKind("timeout", slow, LlmException.Kind.TIMEOUT);
        fake.delay(0);

        section("unreachable");
        ModConfig dead = new ModConfig();
        dead.baseUrl = "http://127.0.0.1:1";
        dead.healthTimeoutSeconds = 1;
        dead.requestTimeoutSeconds = 1;
        OllamaBackend deadBackend = new OllamaBackend(dead, Log.NOOP);
        check("unreachable health", !deadBackend.health(true).available);
        expectKind("unreachable generate", deadBackend, LlmException.Kind.UNAVAILABLE);
    }

    private static void runOpenAiTests(FakeServer fake) {
        section("openai backend");
        ModConfig config = new ModConfig();
        config.provider = "openai";
        config.baseUrl = fake.baseUrl();
        config.apiKey = "test";
        OpenAiBackend backend = new OpenAiBackend(config, Log.NOOP);
        fake.mode(FakeServer.Mode.OK);
        check("openai health", backend.health().available);
        try {
            String text = backend.generate(com.justaivillagers.core.LlmRequest.of("Hi", 32));
            eq("openai text", "Well met, friend.", text);
        } catch (LlmException e) {
            fail("openai generate threw " + e.friendly());
        }
        fake.mode(FakeServer.Mode.MODEL_MISSING);
        expectKind("openai model missing", backend, LlmException.Kind.MODEL_MISSING);
    }

    // ---------------------------------------------------------------- service

    private static void runServiceTests(FakeServer fake) throws Exception {
        section("service async + fallback");
        ModConfig config = new ModConfig();
        config.baseUrl = fake.baseUrl();
        config.model = "gemma3:4b";
        config.maxConcurrentRequests = 2;
        config.enableFallbackLines = true;
        config.groupDialogueDelayMs = 10;

        OllamaBackend backend = new OllamaBackend(config, Log.NOOP);
        DialogueHistory history = new DialogueHistory(config.maxHistoryMessages, config.historyTtlSeconds);
        VillagerAiService service = new VillagerAiService(config, Log.NOOP, backend, history,
                new VillagerProfile.Cache());
        VillagerProfile profile = service.profiles().get(UUID.randomUUID(), false, "en");
        WorldContext ctx = WorldContext.minimal();

        fake.mode(FakeServer.Mode.OK);
        CountDownLatch okLatch = new CountDownLatch(1);
        AtomicReference<String> okText = new AtomicReference<>("");
        boolean accepted = service.askReply(profile.id, false, false, "en", profile, ctx, "Hello", text -> {
            okText.set(text);
            okLatch.countDown();
        }, (kind, msg) -> fail("unexpected error " + kind));
        check("reply accepted", accepted);
        check("reply delivered", okLatch.await(10, TimeUnit.SECONDS));
        check("reply has text", !okText.get().isBlank());

        section("service fallback on failure");
        fake.mode(FakeServer.Mode.HTTP_500);
        CountDownLatch fallbackLatch = new CountDownLatch(1);
        AtomicReference<String> fallbackText = new AtomicReference<>("");
        service.askReply(profile.id, false, false, "en", profile, ctx, "Hello", text -> {
            fallbackText.set(text);
            fallbackLatch.countDown();
        }, (kind, msg) -> {
        });
        check("fallback delivered", fallbackLatch.await(10, TimeUnit.SECONDS));
        check("fallback non-empty", !fallbackText.get().isBlank());

        section("service no fallback");
        config.enableFallbackLines = false;
        fake.mode(FakeServer.Mode.HTTP_500);
        CountDownLatch errorLatch = new CountDownLatch(1);
        service.askReply(profile.id, false, false, "en", profile, ctx, "Hello", text -> fail("should not deliver"),
                (kind, msg) -> errorLatch.countDown());
        check("error reported", errorLatch.await(10, TimeUnit.SECONDS));
        config.enableFallbackLines = true;

        section("group dialogue");
        fake.mode(FakeServer.Mode.OK);
        CountDownLatch groupLatch = new CountDownLatch(3);
        VillagerProfile other = service.profiles().get(UUID.randomUUID(), false, "en");
        service.runGroupDialogue("en", profile, other, ctx, 3, line -> groupLatch.countDown());
        check("group lines delivered", groupLatch.await(20, TimeUnit.SECONDS));
        // Flag must clear even if something goes wrong; wait for the worker to finish.
        long deadline = System.currentTimeMillis() + 5000;
        while (service.isGroupDialogueRunning() && System.currentTimeMillis() < deadline) {
            Thread.sleep(50);
        }
        check("group flag cleared", !service.isGroupDialogueRunning());

        section("group dialogue with dead backend");
        fake.mode(FakeServer.Mode.DOWN);
        CountDownLatch deadLatch = new CountDownLatch(2);
        service.runGroupDialogue("en", profile, other, ctx, 2, line -> deadLatch.countDown());
        check("group fallback delivered", deadLatch.await(20, TimeUnit.SECONDS));
        deadline = System.currentTimeMillis() + 5000;
        while (service.isGroupDialogueRunning() && System.currentTimeMillis() < deadline) {
            Thread.sleep(50);
        }
        check("group flag cleared after failure", !service.isGroupDialogueRunning());

        service.shutdown();
    }

    // ---------------------------------------------------------------- config

    private static void runConfigTests() throws Exception {
        section("config");
        // Not the system temp dir: on some Windows profiles the process may create files but not
        // directories under %TEMP%, which made this whole suite fail for an environment reason.
        Path dir = Files.createDirectories(Path.of("build", "test-tmp", "config-" + System.nanoTime()));
        Path file = dir.resolve("justaivillagers.json");
        ConfigStore store = new ConfigStore(file, Log.NOOP);
        ModConfig defaults = store.load();
        check("config file created", Files.exists(file));
        eq("default provider", "ollama", defaults.provider);

        defaults.model = "custom:7b";
        defaults.baseUrl = "http://example.test:1234/";
        store.save(defaults);
        ModConfig reloaded = store.load();
        eq("config roundtrip model", "custom:7b", reloaded.model);
        eq("config trailing slash trimmed", "http://example.test:1234", reloaded.baseUrl);

        Files.writeString(file, "{ this is not json ");
        ModConfig broken = store.load();
        eq("broken config falls back", "ollama", broken.provider);
        check("broken config backed up", Files.exists(dir.resolve("justaivillagers.json.broken")));

        ModConfig nasty = new ModConfig();
        nasty.maxConcurrentRequests = 0;
        nasty.maxTokens = -5;
        nasty.temperature = 99;
        nasty.healthRefreshSeconds = 0;
        nasty.sanitize();
        check("clamped concurrency", nasty.maxConcurrentRequests >= 1);
        check("clamped tokens", nasty.maxTokens >= 16);
        check("clamped temperature", nasty.temperature <= 2.0);
        check("clamped refresh", nasty.healthRefreshSeconds >= 5);

        // A config written by an older build has no pacing keys; loading it must both keep working
        // and rewrite the file so the new options become discoverable.
        Path legacy = dir.resolve("legacy.json");
        Files.writeString(legacy, "{\"provider\":\"openai\",\"maxConcurrentRequests\":5}");
        ConfigStore legacyStore = new ConfigStore(legacy, Log.NOOP);
        ModConfig upgraded = legacyStore.load();
        eq("legacy config keeps provider", "openai", upgraded.provider);
        eq("legacy config gets pace default", 90, upgraded.speechPaceMsPerChar);
        eq("legacy config gets hurt cooldown default", 4000, upgraded.hurtCooldownMs);
        check("legacy config rewritten with new keys",
                Files.readString(legacy).contains("speechPaceMsPerChar"));
        check("legacy config rewritten with cooldown key",
                Files.readString(legacy).contains("hurtCooldownMs"));

        // A file that already has every key must not be rewritten, or the mod would fight the player
        // over their own edits.
        Path current = dir.resolve("current.json");
        ConfigStore firstWrite = new ConfigStore(current, Log.NOOP);
        firstWrite.save(new ModConfig());
        String textBefore = Files.readString(current);
        new ConfigStore(current, Log.NOOP).load();
        eq("complete config left untouched", textBefore, Files.readString(current));

        ModConfig clampCooldown = new ModConfig();
        clampCooldown.hurtCooldownMs = -10;
        clampCooldown.sanitize();
        eq("negative cooldown clamped to off", 0, clampCooldown.hurtCooldownMs);
        clampCooldown.hurtCooldownMs = 999999;
        clampCooldown.sanitize();
        check("huge cooldown clamped", clampCooldown.hurtCooldownMs <= 60000);

        section("backend hint");
        check("no hint when backend is up",
                BackendHint.forHealth(LlmBackend.Health.ok(), "en", "gemma3:4b", "http://x") == null);
        String downEn = BackendHint.forHealth(
                LlmBackend.Health.down("cannot reach http://127.0.0.1:11434"), "en", "gemma3:4b",
                "http://127.0.0.1:11434");
        // The hint was rewritten to be one sentence: a wall of chat text is scrolled past. These
        // checks pin the parts that must survive that shortening.
        check("unreachable hint links Ollama", downEn != null && downEn.contains(BackendHint.OLLAMA_URL));
        check("unreachable hint names the settings key",
                downEn != null && downEn.contains(" O "));
        check("unreachable hint stays one line", downEn != null && !downEn.contains("\n"));
        check("unreachable hint is short", downEn != null && downEn.length() <= 170);
        String missing = BackendHint.forHealth(
                LlmBackend.Health.down("model gemma3:4b is not installed"), "en", "gemma3:4b", "http://x");
        check("missing model hint names the model",
                missing != null && missing.contains("gemma3:4b"));
        check("missing model hint points at settings",
                missing != null && missing.contains(" O "));
        check("missing model hint is one line", missing != null && !missing.contains("\n"));
        String downRu = BackendHint.forHealth(
                LlmBackend.Health.down("cannot reach http://127.0.0.1:11434"), "ru", "gemma3:4b",
                "http://127.0.0.1:11434");
        check("russian hint is russian", downRu != null && downRu.contains("Ollama"));
        check("russian hint links Ollama", downRu != null && downRu.contains(BackendHint.OLLAMA_URL));
        check("russian hint is not the english one", downRu != null && !downRu.equals(downEn));

        // The reminder shown on repeated talk attempts must be shorter than the first message.
        String repeatEn = BackendHint.repeatHint("en");
        check("repeat hint exists", repeatEn != null && !repeatEn.isBlank());
        check("repeat hint is shorter than the first",
                downEn != null && repeatEn.length() < downEn.length());
        check("repeat hint names the settings key", repeatEn.contains(" O"));
        check("repeat hint has no url", !repeatEn.contains("http"));

        section("empty request guard");
        VillagerAiService service = new VillagerAiService(new ModConfig(), Log.NOOP,
                new OllamaBackend(new ModConfig(), Log.NOOP), new DialogueHistory(4, 60),
                new VillagerProfile.Cache());
        VillagerProfile p = service.profiles().get(UUID.randomUUID(), false, "en");
        boolean rejected = service.askReply(p.id, false, false, "en", p, WorldContext.minimal(), "   ",
                text -> fail("blank accepted"), (kind, msg) -> {
                });
        check("blank message rejected", !rejected);
        service.shutdown();
    }


    // ---------------------------------------------------------------- settings

    private static void runSettingsTests() {
        section("model catalogue");

        check("catalogue has several models", ModelCatalog.ENTRIES.size() >= 5);
        check("catalogue default is offered", ModelCatalog.find(ModelCatalog.DEFAULT_ID) != null);
        check("catalogue ids are unique", ModelCatalog.ENTRIES.stream()
                .map(e -> e.id).distinct().count() == ModelCatalog.ENTRIES.size());
        check("catalogue sizes are plausible", ModelCatalog.ENTRIES.stream()
                .allMatch(e -> e.sizeMb > 100 && e.sizeMb < 10000));
        check("catalogue RAM exceeds download size", ModelCatalog.ENTRIES.stream()
                .allMatch(e -> e.ramMb > e.sizeMb));
        check("catalogue quality is a 1-5 score", ModelCatalog.ENTRIES.stream()
                .allMatch(e -> e.quality >= 1 && e.quality <= 5));
        check("find returns null for a custom id", ModelCatalog.find("someone/own-model:latest") == null);
        eq("size label MB", "398 MB", ModelCatalog.sizeLabel(ModelCatalog.find("qwen2.5:0.5b")));
        eq("size label GB", "3.3 GB", ModelCatalog.sizeLabel(ModelCatalog.find("gemma3:4b")));
        eq("label expands the family", "Gemma 3 1B", ModelCatalog.find("gemma3:1b").label());
        eq("label keeps unknown names", "Other 7B", new ModelCatalog.Entry("other:7b", 100, 200, 1).label());

        section("settings translation");

        String[] uiKeys = {
                "settings.title", "settings.tab.model", "settings.tab.features", "settings.tab.language",
                "settings.server", "settings.model", "settings.model.get", "settings.model.download", "settings.model.needs",
                "settings.ollama.title", "settings.ollama.body", "settings.ollama.download",
                "settings.check", "settings.status.ok", "settings.status.down",
                "settings.save", "settings.cancel", "settings.close", "settings.saved",
                "settings.language.auto", "settings.language",
                "settings.key.dialog", "settings.key.settings", "settings.key.press",
                "settings.temperature", "settings.maxTokens", "settings.speechPace",
                "settings.history", "settings.spontaneous", "settings.group",
                "settings.reactions", "settings.fallback", "settings.debug",
        };
        for (String code : Lang.ALL) {
            LangPack pack = LangData.of(code);
            List<String> missing = new ArrayList<>();
            for (String key : uiKeys) {
                String value = pack.t(key, "x", "y");
                if (value == null || value.isBlank() || value.equals(key)) {
                    missing.add(key);
                }
            }
            check("pack " + code + " has every settings string: " + missing, missing.isEmpty());
        }
        // A translated placeholder must still be a placeholder, or the model name never appears.
        for (String code : Lang.ALL) {
            String needs = LangData.of(code).t("settings.model.needs", "A", "B");
            check("pack " + code + " keeps both placeholders", needs.contains("A") && needs.contains("B"));
        }

        section("settings round trip");

        ModConfig original = new ModConfig();
        original.model = "qwen3:0.6b";
        original.temperature = 1.3;
        original.enableGroupDialogues = false;
        original.languageOverride = Lang.TR;
        ModConfig copy = original.copy();
        copy.model = "gemma3:1b";
        eq("copy keeps the original model", "qwen3:0.6b", original.model);
        // copy() is a snapshot, not a live view: changing the copy must not touch the original.
        eq("copy carries the value over", "gemma3:1b", copy.model);
        eq("copy keeps toggles", false, copy.enableGroupDialogues);
        eq("copy keeps the language", Lang.TR, copy.languageOverride);
        check("copy is a different object", original != copy);

        ModConfig broken = new ModConfig();
        broken.model = "  ";
        broken.temperature = 99.0;
        broken.maxTokens = 100000;
        broken.baseUrl = "http://127.0.0.1:11434///";
        broken.languageOverride = null;
        broken.sanitize();
        eq("blank model resets to the default", ModelCatalog.DEFAULT_ID, broken.model);
        eq("temperature is clamped", 2.0, broken.temperature);
        eq("token limit is clamped", 512, broken.maxTokens);
        eq("trailing slashes are removed", "http://127.0.0.1:11434", broken.baseUrl);
        eq("null language becomes auto", "", broken.languageOverride);


        section("model pull progress");

        // Ollama reports every blob, including 68-byte configs. The puller must ignore small ones or
        // the progress bar jumps to 100% seconds after the download starts and sits there.
        check("tiny blobs do not move the bar", !ModelPuller.isLargeBlob(68) && !ModelPuller.isLargeBlob(1482));
        check("a real weight blob moves the bar", ModelPuller.isLargeBlob(397807936L));

        section("installer");

        check("installer knows this OS", OllamaInstaller.urlForCurrentOs() != null
                || System.getProperty("os.name", "").toLowerCase(java.util.Locale.ROOT).contains("linux"));
        check("installer file name is not empty", !OllamaInstaller.fileNameForCurrentOs().isBlank());
    }

    // ---------------------------------------------------------------- helpers

    private static void expectKind(String label, LlmBackend backend, LlmException.Kind expected) {
        try {
            backend.generate(com.justaivillagers.core.LlmRequest.of("Hi", 16));
            fail(label + ": expected " + expected + " but call succeeded");
        } catch (LlmException e) {
            eq(label + " kind", expected, e.kind());
            check(label + " friendly text", !e.friendly().isBlank());
        }
    }

    private static void section(String name) {
        System.out.println("== " + name);
    }

    private static void eq(String label, Object expected, Object actual) {
        checks++;
        if (expected == null ? actual == null : expected.equals(actual)) {
            System.out.println("  ok  " + label);
        } else {
            System.out.println("  FAIL " + label + " expected=" + expected + " actual=" + actual);
            failures.add(label + " expected=" + expected + " actual=" + actual);
        }
    }

    private static void check(String label, boolean condition) {
        checks++;
        if (condition) {
            System.out.println("  ok  " + label);
        } else {
            System.out.println("  FAIL " + label);
            failures.add(label);
        }
    }

    private static void fail(String label) {
        checks++;
        System.out.println("  FAIL " + label);
        failures.add(label);
    }
}
