package com.justaivillagers.fabric;

import com.justaivillagers.core.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Adapts the core's {@link Log} facade onto SLF4J, which Minecraft already provides. */
public final class Slf4jLog implements Log {

    private final Logger logger;

    public Slf4jLog(String name) {
        this.logger = LoggerFactory.getLogger(name);
    }

    @Override
    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    @Override
    public void error(String message, Throwable error, Object... args) {
        logger.error(message, error);
    }
}
