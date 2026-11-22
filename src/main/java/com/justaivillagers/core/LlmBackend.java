package com.justaivillagers.core;

/**
 * A language backend. Implementations must be thread-safe and must honour the configured timeouts;
 * a stalled model that is never timed out also leaves the group-dialogue flag stuck on.
 */
public interface LlmBackend {

    /**
     * Performs a cheap health check. Implementations may cache the result, but the cache must expire
     * within {@link ModConfig#healthRefreshSeconds} so that starting the server mid-session works.
     */
    Health health();

    /**
     * Runs one generation.
     *
     * @return the model's text, never null (empty string when the model returned nothing)
     * @throws LlmException with a user-presentable reason on any failure
     */
    String generate(LlmRequest request) throws LlmException;

    /** Result of {@link #health()}. */
    final class Health {

        public final boolean available;
        public final String detail;

        /**
         * Model names the server already has, as {@code GET /api/tags} reports them. Used to grey
         * out Download for a model that is installed; cached here so a keystroke does not trigger a
         * second HTTP call. Empty when the server did not answer.
         */
        public final java.util.Set<String> installed;

        public Health(boolean available, String detail) {
            this(available, detail, java.util.Set.of());
        }

        public Health(boolean available, String detail, java.util.Set<String> installed) {
            this.available = available;
            this.detail = detail == null ? "" : detail;
            this.installed = installed == null ? java.util.Set.of() : installed;
        }

        /** True when this exact model (with or without the {@code :latest} tag) is downloaded. */
        public boolean hasModel(String model) {
            if (model == null || model.isBlank()) {
                return false;
            }
            String bare = model.contains(":") ? model : model + ":latest";
            return installed.contains(model) || installed.contains(bare);
        }

        public static Health ok() {
            return new Health(true, "ok");
        }

        public static Health down(String detail) {
            return new Health(false, detail);
        }
    }
}
