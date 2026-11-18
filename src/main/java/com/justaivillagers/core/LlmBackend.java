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

        public Health(boolean available, String detail) {
            this.available = available;
            this.detail = detail == null ? "" : detail;
        }

        public static Health ok() {
            return new Health(true, "ok");
        }

        public static Health down(String detail) {
            return new Health(false, detail);
        }
    }
}
