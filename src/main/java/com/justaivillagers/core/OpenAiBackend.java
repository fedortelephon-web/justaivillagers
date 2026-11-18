package com.justaivillagers.core;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * Any OpenAI-compatible endpoint ({@code POST /v1/chat/completions}): LM Studio, llama.cpp server,
 * OpenRouter, DeepSeek, vLLM, or Ollama's own compatibility layer. This is what turns the mod from
 * "requires Ollama" into "requires any model".
 */
public final class OpenAiBackend implements LlmBackend {

    private static final Gson GSON = new Gson();

    private final ModConfig config;
    private final Log log;
    private final HttpClient http;

    public OpenAiBackend(ModConfig config, Log log) {
        this.config = config;
        this.log = log;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(Math.max(1, config.healthTimeoutSeconds)))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Override
    public Health health() {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(config.baseUrl + "/v1/models"))
                    .timeout(Duration.ofSeconds(config.healthTimeoutSeconds))
                    .GET();
            if (!config.apiKey.isBlank()) {
                builder.header("Authorization", "Bearer " + config.apiKey);
            }
            HttpResponse<String> resp = http.send(builder.build(),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (resp.statusCode() >= 400) {
                return Health.down("HTTP " + resp.statusCode());
            }
            return Health.ok();
        } catch (HttpTimeoutException e) {
            return Health.down("health check timed out");
        } catch (IOException e) {
            return Health.down("cannot reach " + config.baseUrl);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Health.down("interrupted");
        }
    }

    @Override
    public String generate(LlmRequest request) throws LlmException {
        JsonObject payload = new JsonObject();
        payload.addProperty("model", config.model);
        payload.addProperty("temperature", request.temperature);
        payload.addProperty("max_tokens", request.maxTokens);
        payload.addProperty("stream", false);

        JsonArray messages = new JsonArray();
        if (request.systemPrompt != null && !request.systemPrompt.isEmpty()) {
            JsonObject system = new JsonObject();
            system.addProperty("role", "system");
            system.addProperty("content", request.systemPrompt);
            messages.add(system);
        }
        if (request.history != null) {
            for (ChatTurn turn : request.history) {
                JsonObject msg = new JsonObject();
                msg.addProperty("role", turn.role.wireName());
                msg.addProperty("content", turn.text);
                messages.add(msg);
            }
        }
        JsonObject user = new JsonObject();
        user.addProperty("role", "user");
        user.addProperty("content", request.userPrompt);
        messages.add(user);
        payload.add("messages", messages);

        String body = GSON.toJson(payload);
        if (config.debugPrompts) {
            log.info("[prompt] {}", body);
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(config.baseUrl + "/v1/chat/completions"))
                .timeout(Duration.ofSeconds(config.requestTimeoutSeconds))
                .header("Content-Type", "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8));
        if (!config.apiKey.isBlank()) {
            builder.header("Authorization", "Bearer " + config.apiKey);
        }

        HttpResponse<String> resp;
        try {
            resp = http.send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (HttpTimeoutException e) {
            throw new LlmException(LlmException.Kind.TIMEOUT, "timed out after " + config.requestTimeoutSeconds + "s", e);
        } catch (IOException e) {
            throw new LlmException(LlmException.Kind.UNAVAILABLE, "cannot reach " + config.baseUrl, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LlmException(LlmException.Kind.OTHER, "interrupted", e);
        }

        if (config.debugPrompts) {
            log.info("[response] {}", resp.body());
        }
        if (resp.statusCode() == 401 || resp.statusCode() == 403) {
            throw new LlmException(LlmException.Kind.HTTP_ERROR, "API key rejected");
        }
        if (resp.statusCode() == 404) {
            throw new LlmException(LlmException.Kind.MODEL_MISSING, "model " + config.model + " not found");
        }
        if (resp.statusCode() >= 400) {
            throw new LlmException(LlmException.Kind.HTTP_ERROR, "HTTP " + resp.statusCode());
        }
        return extractText(resp.body());
    }

    private String extractText(String body) throws LlmException {
        JsonObject root;
        try {
            root = JsonParser.parseString(body).getAsJsonObject();
        } catch (JsonParseException | IllegalStateException e) {
            throw new LlmException(LlmException.Kind.BAD_RESPONSE, "response was not JSON", e);
        }
        if (root.has("error") && root.get("error").isJsonObject()) {
            JsonObject err = root.getAsJsonObject("error");
            String message = err.has("message") ? err.get("message").getAsString() : "unknown error";
            throw new LlmException(LlmException.Kind.HTTP_ERROR, message);
        }
        if (!root.has("choices") || !root.get("choices").isJsonArray()) {
            throw new LlmException(LlmException.Kind.BAD_RESPONSE, "no choices field");
        }
        JsonArray choices = root.getAsJsonArray("choices");
        if (choices.isEmpty() || !choices.get(0).isJsonObject()) {
            throw new LlmException(LlmException.Kind.BAD_RESPONSE, "empty choices");
        }
        JsonObject choice = choices.get(0).getAsJsonObject();
        if (!choice.has("message") || !choice.get("message").isJsonObject()) {
            // Some servers answer with {"choices":[{"text":"..."}]}
            if (choice.has("text") && choice.get("text").isJsonPrimitive()) {
                return TextSanitizer.cleanForChat(choice.get("text").getAsString());
            }
            throw new LlmException(LlmException.Kind.BAD_RESPONSE, "no message field");
        }
        JsonObject message = choice.getAsJsonObject("message");
        if (!message.has("content") || !message.get("content").isJsonPrimitive()) {
            throw new LlmException(LlmException.Kind.BAD_RESPONSE, "no content field");
        }
        return TextSanitizer.cleanForChat(message.get("content").getAsString());
    }
}
