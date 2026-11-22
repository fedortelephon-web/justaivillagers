package com.justaivillagers.core;

/**
 * A language backend. Implementations must be thread-safe and must honour the configured timeouts:
 * the original build hung forever when the model stalled, which then left the group-dialogue flag
 * stuck on.
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
         * Names of the models the server already has, as {@code GET /api/tags} reports them.
         *
         * <p>The settings screen needs this to grey out Download on a model that is already there;
         * asking the backend twice for the same answer would mean a second HTTP call per keystroke.
         * Empty when the server did not answer, which is also the right answer then.
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
