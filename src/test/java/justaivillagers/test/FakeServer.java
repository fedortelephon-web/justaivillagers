package justaivillagers.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Tiny fake Ollama / OpenAI server used by {@link CoreTests}. No external dependencies, so the core
 * can be verified without Minecraft or a real model.
 */
final class FakeServer implements AutoCloseable {

    enum Mode {
        OK,
        DOWN,
        MODEL_MISSING,
        HTTP_500,
        TIMEOUT,
        BAD_JSON,
        QUOTED,
        MARKDOWN
    }

    private final HttpServer server;
    private final java.util.concurrent.ExecutorService pool;
    private volatile Mode mode = Mode.OK;
    private final AtomicInteger generateCalls = new AtomicInteger();
    private final AtomicInteger tagsCalls = new AtomicInteger();
    private volatile long delayMillis = 0L;

    FakeServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/api/tags", this::handleTags);
        server.createContext("/api/generate", this::handleGenerate);
        server.createContext("/v1/models", this::handleTags);
        server.createContext("/v1/chat/completions", this::handleOpenAi);
        server.setExecutor(pool = java.util.concurrent.Executors.newFixedThreadPool(4, runnable -> {
            Thread thread = new Thread(runnable, "fake-server");
            thread.setDaemon(true);
            return thread;
        }));
        server.start();
    }

    String baseUrl() {
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    FakeServer mode(Mode mode) {
        this.mode = mode;
        return this;
    }

    FakeServer delay(long millis) {
        this.delayMillis = millis;
        return this;
    }

    int generateCalls() {
        return generateCalls.get();
    }

    int tagsCalls() {
        return tagsCalls.get();
    }

    private void handleTags(HttpExchange exchange) throws IOException {
        tagsCalls.incrementAndGet();
        if (mode == Mode.DOWN) {
            respond(exchange, 503, "{\"error\":\"down\"}");
            return;
        }
        if (mode == Mode.MODEL_MISSING) {
            respond(exchange, 200, "{\"models\":[{\"name\":\"llama3:8b\"}]}");
            return;
        }
        if (exchange.getRequestURI().getPath().equals("/v1/models")) {
            respond(exchange, 200, "{\"data\":[{\"id\":\"gemma3:4b\"}]}");
            return;
        }
        respond(exchange, 200, "{\"models\":[{\"name\":\"gemma3:4b\"},{\"name\":\"llama3:8b\"}]}");
    }

    private void handleGenerate(HttpExchange exchange) throws IOException {
        generateCalls.incrementAndGet();
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        sleepQuietly(delayMillis);
        switch (mode) {
            case DOWN:
                respond(exchange, 503, "{\"error\":\"connection refused\"}");
                return;
            case MODEL_MISSING:
                respond(exchange, 404, "{\"error\":\"model 'gemma3:4b' not found\"}");
                return;
            case HTTP_500:
                respond(exchange, 500, "{\"error\":\"internal\"}");
                return;
            case BAD_JSON:
                respond(exchange, 200, "<html>nope</html>");
                return;
            case QUOTED:
                respond(exchange, 200, "{\"response\":\"\\\"Hello there, traveler!\\\"\"}");
                return;
            case MARKDOWN:
                respond(exchange, 200, "{\"response\":\"**As an AI language model:** Good day!\"}");
                return;
            case OK:
            default:
                String echo = body.contains("Игрок говорит") || body.contains("\u0418\u0433\u0440\u043e\u043a")
                        ? "Да, слышу тебя."
                        : "Aye, good day to you.";
                respond(exchange, 200, "{\"response\":\"" + echo + "\"}");
        }
    }

    private void handleOpenAi(HttpExchange exchange) throws IOException {
        generateCalls.incrementAndGet();
        exchange.getRequestBody().readAllBytes();
        sleepQuietly(delayMillis);
        if (mode == Mode.MODEL_MISSING) {
            respond(exchange, 404, "{\"error\":{\"message\":\"no such model\"}}");
            return;
        }
        if (mode == Mode.BAD_JSON) {
            respond(exchange, 200, "not json at all");
            return;
        }
        respond(exchange, 200,
                "{\"choices\":[{\"message\":{\"role\":\"assistant\",\"content\":\"Well met, friend.\"}}]}");
    }

    private static void sleepQuietly(long millis) {
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    @Override
    public void close() {
        server.stop(0);
        pool.shutdownNow();
    }
}
