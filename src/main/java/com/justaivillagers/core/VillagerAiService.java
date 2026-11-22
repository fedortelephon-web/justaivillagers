package com.justaivillagers.core;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Orchestrates generation: bounded concurrency, named worker threads, fallback lines, per-villager
 * throttling and a single place where errors become player-visible text.
 *
 * <p>Requests are not fired per tick per villager on the common ForkJoin pool ({@code 2e-4}
 * chance times 15 villagers is a lot of simultaneous LLM calls), used {@code Thread.sleep} to pace
 * dialogue, swallowed exceptions and could leave the group-dialogue flag stuck forever. All of that
 * is concentrated here now.
 */
public final class VillagerAiService {

    /** Callback invoked on a worker thread; the game layer must hop back to the client thread. */
    public interface Callback {
        void onLine(String text);
    }

    public interface ErrorCallback {
        void onError(LlmException.Kind kind, String friendlyMessage);
    }

    private final ModConfig config;
    private final Log log;
    private final LlmBackend backend;
    private final DialogueHistory history;
    private final VillagerProfile.Cache profiles;
    private final ExecutorService workers;
    private final Semaphore permits;

    private final AtomicBoolean groupRunning = new AtomicBoolean(false);
    private final AtomicLong lastHealthLog = new AtomicLong(0L);

    public VillagerAiService(ModConfig config, Log log, LlmBackend backend, DialogueHistory history,
                             VillagerProfile.Cache profiles) {
        this.config = config;
        this.log = log;
        this.backend = backend;
        this.history = history;
        this.profiles = profiles;
        this.permits = new Semaphore(Math.max(1, config.maxConcurrentRequests));
        ThreadFactory factory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger();

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "justaivillagers-" + counter.incrementAndGet());
                thread.setDaemon(true);
                return thread;
            }
        };
        this.workers = Executors.newFixedThreadPool(Math.max(2, config.maxConcurrentRequests), factory);
    }

    public LlmBackend backend() {
        return backend;
    }

    public DialogueHistory history() {
        return history;
    }

    public VillagerProfile.Cache profiles() {
        return profiles;
    }

    public boolean isGroupDialogueRunning() {
        return groupRunning.get();
    }

    /**
     * Asks a villager to answer the player. Never blocks the caller.
     *
     * @return false when the request was dropped (too many in flight)
     */
    public boolean askReply(UUID villagerId, boolean child, boolean sleeping, String lang, VillagerProfile profile,
                            WorldContext ctx, String playerMessage, Callback onLine, ErrorCallback onError) {
        if (playerMessage == null || playerMessage.isBlank()) {
            return false;
        }
        history.add(villagerId, ChatTurn.Role.USER, playerMessage);
        String sessionLabel = historyLabel(lang, villagerId, profile);
        LlmRequest request = sleeping
                ? PromptBuilder.sleeping(config, lang, profile, ctx, playerMessage, history, sessionLabel)
                : PromptBuilder.reply(config, lang, profile, ctx, playerMessage, history, sessionLabel);
        return run(villagerId, lang, profile, ctx, request, onLine, onError,
                () -> FallbackLines.forSleeping(lang));
    }

    public boolean askReaction(UUID villagerId, String eventKey, String mobName, String lang, VillagerProfile profile,
                               WorldContext ctx, Callback onLine, ErrorCallback onError) {
        // No transcript on purpose: a reaction is a shout in the moment. Feeding the last exchange
        // made the model answer the previous question again instead of reacting to the hit or the mob.
        LlmRequest request = PromptBuilder.reaction(config, lang, profile, ctx, eventKey, mobName, history, null);
        return run(villagerId, lang, profile, ctx, request, onLine, onError,
                () -> FallbackLines.forReaction(lang, eventKey, profile, mobName));
    }

    public boolean askSpontaneous(UUID villagerId, String lang, VillagerProfile profile, WorldContext ctx,
                                  Callback onLine, ErrorCallback onError) {
        // Same reason as reactions: no transcript. Handing the model its own previous mutterings made
        // it answer itself ("you're still saying that gibberish") and repeat one phrasing forever.
        LlmRequest request = PromptBuilder.spontaneous(config, lang, profile, ctx, history, null);
        return run(villagerId, lang, profile, ctx, request, onLine, onError,
                () -> FallbackLines.forSpontaneous(lang, profile, ctx));
    }

    /**
     * Runs an evening conversation between two villagers, line by line. Uses the worker thread and
     * polling with a deadline instead of {@code Thread.sleep}, and always clears the running flag.
     */
    public void runGroupDialogue(String lang, VillagerProfile first, VillagerProfile second, WorldContext ctx,
                                 int lines, Callback onLine, java.util.function.BooleanSupplier stillThere) {
        if (!groupRunning.compareAndSet(false, true)) {
            return;
        }
        try {
            workers.execute(() -> {
                try {
                    List<String[]> transcript = new java.util.ArrayList<>();
                    String[] speakers = {first.name, second.name};
                    VillagerProfile[] profilesByTurn = {first, second};
                    for (int i = 0; i < lines; i++) {
                        if (Thread.currentThread().isInterrupted()) {
                            return;
                        }
                        // A conversation needs two living villagers. If one dies mid-evening, the
                        // exchange stops instead of letting the survivor talk to a corpse.
                        if (stillThere != null && !stillThere.getAsBoolean()) {
                            log.info("Group dialogue stopped: a speaker is gone");
                            return;
                        }
                        VillagerProfile speaker = profilesByTurn[i % 2];
                        VillagerProfile listener = profilesByTurn[(i + 1) % 2];
                        String text = null;
                        try {
                            LlmRequest req = PromptBuilder.groupLine(config, lang, speaker, ctx, listener,
                                    transcript, null);
                            text = backend.generate(req);
                        } catch (LlmException e) {
                            log.warn("Group dialogue line {} failed: {}", i, e.friendly());
                        }
                        if (text == null || text.isBlank()) {
                            if (!config.enableFallbackLines) {
                                break;
                            }
                            text = FallbackLines.forGroupLine(lang, speaker, ctx);
                        }
                        // Death can land between the check above and this line, so ask again right
                        // before the text reaches the chat.
                        if (stillThere != null && !stillThere.getAsBoolean()) {
                            return;
                        }
                        transcript.add(new String[]{speaker.name, text});
                        String line = "[" + speaker.name + " → " + listener.name + "] " + text;
                        onLine.onLine(line);
                        // Hold the next turn back for as long as this line would take to say, never
                        // less than the configured minimum gap, so the exchange reads like speech
                        // instead of arriving as a wall of text.
                        if (i < lines - 1) {
                            long pace = Math.max(SpeechPace.pauseFor(text, config),
                                    config.groupDialogueDelayMs);
                            if (!pause(pace)) {
                                return;
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    log.error("Group dialogue crashed", e);
                } finally {
                    groupRunning.set(false);
                }
            });
        } catch (RejectedExecutionException e) {
            groupRunning.set(false);
            log.warn("Group dialogue rejected by executor");
        }
    }

    private static boolean pause(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private interface FallbackSupplier {
        String get();
    }

    private boolean run(UUID villagerId, String lang, VillagerProfile profile, WorldContext ctx, LlmRequest request,
                        Callback onLine, ErrorCallback onError, FallbackSupplier fallback) {
        if (!permits.tryAcquire()) {
            log.warn("Dropping request for {}: {} already in flight", profile.name, config.maxConcurrentRequests);
            if (onError != null) {
                onError.onError(LlmException.Kind.OVERLOADED, "too many requests in flight");
            }
            return false;
        }
        try {
            workers.execute(() -> {
                try {
                    String text;
                    try {
                        text = backend.generate(request);
                    } catch (LlmException e) {
                        log.warn("Generation for {} failed: {} ({})", profile.name, e.friendly(), e.kind());
                        if (onError != null) {
                            onError.onError(e.kind(), e.friendly());
                        }
                        if (!config.enableFallbackLines) {
                            return;
                        }
                        text = fallback.get();
                    }
                    if (text == null || text.isBlank()) {
                        if (!config.enableFallbackLines) {
                            return;
                        }
                        text = fallback.get();
                    }
                    // Models that answer in transcript form put "Name: " in front of the line, and
                    // chat already prints the name itself.
                    text = TextSanitizer.stripSpeakerPrefix(text, profile.name);
                    history.add(villagerId, ChatTurn.Role.ASSISTANT, text);
                    if (onLine != null) {
                        onLine.onLine(text);
                    }
                } catch (RuntimeException e) {
                    log.error("Unexpected failure while asking " + profile.name, e);
                } finally {
                    permits.release();
                }
            });
            return true;
        } catch (RejectedExecutionException e) {
            permits.release();
            log.warn("Executor rejected request for {}", profile.name);
            return false;
        }
    }

    private String historyLabel(String lang, UUID villagerId, VillagerProfile profile) {
        LangPack pack = LangData.of(Lang.normalise(lang));
        String transcript = history.format(villagerId, config.maxHistoryMessages,
                pack.t("player"), profile.name);
        if (transcript.isEmpty()) {
            return null;
        }
        return pack.t("history_label") + transcript;
    }

    /** Current backend state, for the world-join hint. Never throws; a down backend is a value. */
    public LlmBackend.Health health() {
        return backend.health();
    }

    /**
     * Writes the backend state to the log at most once a minute.
     *
     * <p>Called on world join so a bug report shows which endpoint and model were in use, even when
     * everything worked. The chat message stays reserved for actual failures.
     */
    public void logHealthOnce() {
        long now = System.currentTimeMillis();
        if (now - lastHealthLog.get() < 60_000L) {
            return;
        }
        lastHealthLog.set(now);
        LlmBackend.Health health = backend.health();
        if (health.available) {
            log.info("Backend OK: {} at {} (model {})", config.provider, config.baseUrl, config.model);
        } else {
            log.warn("Backend unavailable: {}", health.detail);
        }
    }

    public void shutdown() {
        workers.shutdownNow();
        try {
            workers.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clearAll() {
        history.clearAll();
        profiles.clear();
    }
}
