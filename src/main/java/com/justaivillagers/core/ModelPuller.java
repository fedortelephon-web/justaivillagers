package com.justaivillagers.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Downloads a model into a running Ollama via {@code POST /api/pull}.
 *
 * <p>This exists so a player never has to open a terminal. The alternative - telling them to run
 * {@code ollama pull gemma3:4b} - is where most people give up, because it assumes they know what a
 * terminal is and that Ollama is already installed.
 *
 * <p>Ollama streams newline-delimited JSON: {@code {"status":"pulling manifest"}}, then progress
 * objects with {@code completed} and {@code total} byte counts, then {@code {"status":"success"}}.
 * The download runs on its own thread and reports back through {@link Listener}; nothing blocks the
 * game thread.
 */
public final class ModelPuller {

    /** Progress callback, always invoked off the game thread. */
    public interface Listener {

        /** A new phase started, for example "pulling manifest" or "verifying sha256 digest". */
        void onStatus(String text);

        /** Bytes received so far and the total, or a negative total when unknown. */
        void onProgress(long completed, long total);

        /** The model is usable now. */
        void onDone();

        /** The pull failed; the message is fit to show a player. */
        void onError(String message);
    }

    private final ModConfig config;
    private final Log log;
    private volatile boolean cancelled;

    /** Blobs smaller than this are config, templates and manifests, not model weights. */
    private static final long LARGE_BLOB_BYTES = 4L * 1024 * 1024;

    /** Whether a blob of this size is model weights worth showing progress for. */
    public static boolean isLargeBlob(long totalBytes) {
        return totalBytes >= LARGE_BLOB_BYTES;
    }

    public ModelPuller(ModConfig config, Log log) {
        this.config = config;
        this.log = log;
    }

    /** Stops the download at the next chunk. The game must stay responsive even mid-pull. */
    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Pulls a model. Returns immediately; the work happens on a daemon thread so a closed screen
     * does not leave a non-daemon thread holding the JVM open.
     */
    public void pull(String model, Listener listener) {
        Thread thread = new Thread(() -> run(model, listener), "justaivillagers-pull");
        thread.setDaemon(true);
        thread.start();
    }

    private void run(String model, Listener listener) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("model", model);
            body.addProperty("stream", true);

            HttpClient http = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.baseUrl + "/api/pull"))
                    .timeout(Duration.ofHours(2))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<InputStream> response = http.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() >= 400) {
                String detail = readAll(response.body());
                listener.onError("HTTP " + response.statusCode() + ": " + trim(detail));
                return;
            }
            stream(response.body(), listener);
        } catch (java.net.http.HttpTimeoutException e) {
            listener.onError("download timed out");
        } catch (IOException e) {
            if (cancelled) {
                return;
            }
            log.warn("Model pull failed: {}", e.toString());
            listener.onError("cannot reach " + config.baseUrl);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void stream(InputStream in, Listener listener) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (cancelled) {
                    listener.onError("cancelled");
                    return;
                }
                if (line.isBlank()) {
                    continue;
                }
                JsonObject obj;
                try {
                    obj = JsonParser.parseString(line).getAsJsonObject();
                } catch (RuntimeException e) {
                    // A malformed line mid-stream is not worth failing the whole download over.
                    continue;
                }
                if (obj.has("error")) {
                    listener.onError(obj.get("error").getAsString());
                    return;
                }
                String status = obj.has("status") ? obj.get("status").getAsString() : "";
                long completed = obj.has("completed") ? obj.get("completed").getAsLong() : -1L;
                long total = obj.has("total") ? obj.get("total").getAsLong() : -1L;

                if ("success".equals(status)) {
                    listener.onDone();
                    return;
                }
                if (completed >= 0 && total > 0) {
                    // Ollama reports every blob, including 68-byte config files, so a naive progress
                    // bar hits 100% seconds after the download starts and sits there. Only the big
                    // weight blobs move the bar; small ones are just status noise.
                    if (isLargeBlob(total)) {
                        listener.onProgress(completed, total);
                    }
                } else if (!status.isEmpty()) {
                    listener.onStatus(status);
                }
            }
        }
        // A stream that ends without "success" still means the pull finished on older daemons that
        // did not send a final object. Treat it as done rather than hanging on a closed screen.
        listener.onDone();
    }

    private static String readAll(InputStream in) {
        try (InputStream stream = in) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    private static String trim(String s) {
        if (s == null) {
            return "";
        }
        String one = s.replace('\n', ' ').trim();
        return one.length() <= 160 ? one : one.substring(0, 160) + "...";
    }
}
