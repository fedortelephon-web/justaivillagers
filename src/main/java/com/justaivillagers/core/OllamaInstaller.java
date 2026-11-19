package com.justaivillagers.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

/**
 * Downloads the Ollama installer for the current OS.
 *
 * <p>Deliberately does <em>not</em> run the installer. Executing a downloaded binary from inside a
 * mod would be an unexpected thing for a Minecraft client to do: it trips antivirus heuristics,
 * Windows shows a SmartScreen prompt the player did not ask for, and a mod that silently launches
 * executables is exactly the behaviour malware uses. The file is saved and its path shown, and the
 * player double-clicks it. One extra step, no surprises.
 */
public final class OllamaInstaller {

    /** Where the download page lives, for the "open in browser" path. */
    public static final String DOWNLOAD_PAGE = "https://ollama.com/download";

    /** Progress callback, always invoked off the game thread. */
    public interface Listener {

        void onProgress(long completed, long total);

        /** @param saved absolute path of the downloaded file */
        void onDone(Path saved);

        void onError(String message);
    }

    private final Log log;
    private volatile boolean cancelled;

    public OllamaInstaller(Log log) {
        this.log = log;
    }

    public void cancel() {
        cancelled = true;
    }

    /** The installer URL for the OS the game is running on, or {@code null} when unsupported. */
    public static String urlForCurrentOs() {
        String os = System.getProperty("os.name", "").toLowerCase(java.util.Locale.ROOT);
        if (os.contains("win")) {
            return "https://ollama.com/download/OllamaSetup.exe";
        }
        if (os.contains("mac")) {
            return "https://ollama.com/download/Ollama-darwin.zip";
        }
        // Linux shipping is a shell script, not an installer file, so browser is the honest answer.
        return null;
    }

    public static String fileNameForCurrentOs() {
        String os = System.getProperty("os.name", "").toLowerCase(java.util.Locale.ROOT);
        if (os.contains("win")) {
            return "OllamaSetup.exe";
        }
        if (os.contains("mac")) {
            return "Ollama-darwin.zip";
        }
        return "ollama-installer";
    }

    /**
     * Downloads the installer into {@code dir}. Returns immediately; work happens on a daemon thread.
     */
    public void download(Path dir, Listener listener) {
        Thread thread = new Thread(() -> run(dir, listener), "justaivillagers-installer");
        thread.setDaemon(true);
        thread.start();
    }

    private void run(Path dir, Listener listener) {
        String url = urlForCurrentOs();
        if (url == null) {
            listener.onError("no installer for this OS");
            return;
        }
        Path target = dir.resolve(fileNameForCurrentOs());
        Path part = dir.resolve(fileNameForCurrentOs() + ".part");
        try {
            Files.createDirectories(dir);
            HttpClient http = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(15))
                    // ollama.com redirects to a signed GitHub release asset.
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofHours(1))
                    .GET()
                    .build();
            HttpResponse<InputStream> response = http.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() >= 400) {
                listener.onError("HTTP " + response.statusCode());
                return;
            }
            long total = response.headers().firstValueAsLong("content-length").orElse(-1L);
            copy(response.body(), part, total, listener);
            if (cancelled) {
                Files.deleteIfExists(part);
                return;
            }
            // Rename only after a complete download, so a half-written exe is never left behind
            // looking runnable.
            Files.move(part, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            listener.onDone(target);
        } catch (IOException e) {
            if (!cancelled) {
                log.warn("Installer download failed: {}", e.toString());
                listener.onError(e.getMessage() == null ? "download failed" : e.getMessage());
            }
            try {
                Files.deleteIfExists(part);
            } catch (IOException ignored) {
                // Cleanup is best effort.
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void copy(InputStream in, Path target, long total, Listener listener) throws IOException {
        try (InputStream stream = in; OutputStream out = Files.newOutputStream(target)) {
            byte[] buffer = new byte[64 * 1024];
            long done = 0L;
            int read;
            long lastReport = 0L;
            while ((read = stream.read(buffer)) != -1) {
                if (cancelled) {
                    return;
                }
                out.write(buffer, 0, read);
                done += read;
                // Report at most every 256 KB: a per-chunk callback would flood the render thread.
                if (done - lastReport >= 256 * 1024 || done == total) {
                    lastReport = done;
                    listener.onProgress(done, total);
                }
            }
        }
    }
}
