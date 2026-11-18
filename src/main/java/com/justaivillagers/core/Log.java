package com.justaivillagers.core;

/**
 * Minimal logging facade so the pure-Java core does not depend on SLF4J or Minecraft.
 */
public interface Log {

    void info(String message, Object... args);

    void warn(String message, Object... args);

    void error(String message, Throwable error, Object... args);

    /** Discards everything. Useful for unit tests. */
    Log NOOP = new Log() {
        @Override
        public void info(String message, Object... args) {
        }

        @Override
        public void warn(String message, Object... args) {
        }

        @Override
        public void error(String message, Throwable error, Object... args) {
        }
    };
}
