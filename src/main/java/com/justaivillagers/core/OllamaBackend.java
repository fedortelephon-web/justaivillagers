package com.justaivillagers.core;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * Native Ollama backend ({@code POST /api/generate}, health via {@code GET /api/tags}).
 *
 * <p>Compared to the original implementation this uses JSON serialisation instead of regex parsing,
 * sets a request timeout, distinguishes "server down" from "model missing", sends
 * {@code num_predict} and {@code keep_alive}, and caches the health probe with an expiry.
 */
public final class OllamaBackend implements LlmBackend {

    private static final Gson GSON = new Gson();

    private final ModConfig config;
    private final Log log;
    private final HttpClient http;
    private final AtomicReference<Health> cachedHealth = new AtomicReference<>(null);
    private final AtomicLong cachedAt = new AtomicLong(0L);

    public OllamaBackend(ModConfig config, Log log) {
        this.config = config;
        this.log = log;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(Math.max(1, config.healthTimeoutSeconds)))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Override
    public Health health() {
        return health(false);
    }

    /** @param force ignore the cached value (used by the "retry" button and on world join) */
    public Health health(boolean force) {
        long now = System.currentTimeMillis();
        Health cached = cachedHealth.get();
        if (!force && cached != null && now - cachedAt.get() < config.healthRefreshSeconds * 1000L) {
            return cached;
        }
        Health fresh;
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(config.baseUrl + "/api/tags"))
                    .timeout(Duration.ofSeconds(config.healthTimeoutSeconds))
                    .GET()
                    .build();
            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (resp.statusCode() != 200) {
                fresh = Health.down("HTTP " + resp.statusCode());
            } else {
                java.util.Set<String> installed = installedModels(resp.body());
                if (!installed.contains(config.model) && !installed.contains(latestTag(config.model))) {
                    fresh = new Health(false, "model " + config.model + " is not installed", installed);
                } else {
                    fresh = new Health(true, "ok", installed);
                }
            }
        } catch (HttpTimeoutException e) {
            fresh = Health.down("health check timed out");
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            fresh = Health.down("cannot reach " + config.baseUrl);
        }
        cachedHealth.set(fresh);
        cachedAt.set(now);
        return fresh;
    }

    /** {@code name} with a {@code :latest} tag if it has none, the way Ollama reports models. */
    static String latestTag(String name) {
        return name != null && !name.contains(":") ? name + ":latest" : name;
    }

    /**
     * Every model name in a {@code /api/tags} response.
     *
     * <p>Static and total: a malformed body yields an empty set rather than an exception, because
     * the caller is a health probe whose only job is to answer yes or no.
     */
    static java.util.Set<String> installedModels(String body) {
        java.util.Set<String> names = new java.util.HashSet<>();
        if (body == null || body.isBlank()) {
            return names;
        }
        try {
            JsonObject root = JsonParser.parseString(body).getAsJsonObject();
            if (!root.has("models") || !root.get("models").isJsonArray()) {
                return names;
            }
            for (com.google.gson.JsonElement el : root.getAsJsonArray("models")) {
                if (!el.isJsonObject()) {
                    continue;
                }
                JsonObject model = el.getAsJsonObject();
                for (String key : new String[]{"name", "model"}) {
                    if (model.has(key) && model.get(key).isJsonPrimitive()) {
                        names.add(model.get(key).getAsString());
                    }
                }
            }
        } catch (JsonParseException | IllegalStateException e) {
            return names;
        }
        return names;
    }

    @Override
    public String generate(LlmRequest request) throws LlmException {
        JsonObject payload = new JsonObject();
        payload.addProperty("model", config.model);
        payload.addProperty("prompt", request.userPrompt);
        if (request.systemPrompt != null && !request.systemPrompt.isEmpty()) {
            payload.addProperty("system", request.systemPrompt);
        }
        payload.addProperty("stream", false);
        payload.addProperty("temperature", request.temperature);
        payload.addProperty("num_predict", request.maxTokens);
        if (config.keepAlive != null && !config.keepAlive.isBlank()) {
            payload.addProperty("keep_alive", config.keepAlive);
        }

        String body = GSON.toJson(payload);
        if (config.debugPrompts) {
            // The toggle is now "write prompts to chat": the log was never where a player looks, and
            // the question it answers ("what exactly was sent when I said this?") is asked while
            // playing. The full JSON stays in the log for bug reports.
            log.info("[prompt] {}", body);
            // Only the player's own line goes to chat; the assembled prompt above is a screenful of
            // context and would bury it.
            DebugChat.emit(request.playerLine);
        }

        String responseBody = post("/api/generate", body);
        if (config.debugPrompts) {
            log.info("[response] {}", responseBody);
        }
        return extractText(responseBody);
    }

    private String extractText(String responseBody) throws LlmException {
        JsonObject root;
        try {
            root = JsonParser.parseString(responseBody).getAsJsonObject();
        } catch (JsonParseException | IllegalStateException e) {
            throw new LlmException(LlmException.Kind.BAD_RESPONSE, "response was not JSON", e);
        }
        if (root.has("error") && root.get("error").isJsonPrimitive()) {
            String err = root.get("error").getAsString();
            LlmException.Kind kind = err.toLowerCase().contains("not found")
                    ? LlmException.Kind.MODEL_MISSING
                    : LlmException.Kind.HTTP_ERROR;
            throw new LlmException(kind, err);
        }
        if (!root.has("response") || !root.get("response").isJsonPrimitive()) {
            throw new LlmException(LlmException.Kind.BAD_RESPONSE, "no response field");
        }
        return TextSanitizer.cleanForChat(root.get("response").getAsString());
    }

    /** Shared POST helper with proper timeout and error classification. */
    private String post(String path, String body) throws LlmException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(config.baseUrl + path))
                .timeout(Duration.ofSeconds(config.requestTimeoutSeconds))
                .header("Content-Type", "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
        try {
            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (resp.statusCode() == 404) {
                throw new LlmException(LlmException.Kind.MODEL_MISSING, "HTTP 404: " + truncate(resp.body()));
            }
            if (resp.statusCode() >= 400) {
                throw new LlmException(LlmException.Kind.HTTP_ERROR, "HTTP " + resp.statusCode() + ": " + truncate(resp.body()));
            }
            return resp.body();
        } catch (HttpTimeoutException e) {
            throw new LlmException(LlmException.Kind.TIMEOUT, "timed out after " + config.requestTimeoutSeconds + "s", e);
        } catch (IOException e) {
            throw new LlmException(LlmException.Kind.UNAVAILABLE, "cannot reach " + config.baseUrl, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LlmException(LlmException.Kind.OTHER, "interrupted", e);
        }
    }

    private static String truncate(String s) {
        if (s == null) {
            return "";
        }
        return s.length() <= 200 ? s : s.substring(0, 200) + "...";
    }
}
