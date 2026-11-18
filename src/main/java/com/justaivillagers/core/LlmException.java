package com.justaivillagers.core;

/** Failure of a language backend, carrying a short reason that is safe to show in chat. */
public class LlmException extends Exception {

    private static final long serialVersionUID = 1L;

    public enum Kind {
        /** Nothing is listening on the configured URL. */
        UNAVAILABLE,
        /** The server answered, but with an error status. */
        HTTP_ERROR,
        /** The model was not installed / not loaded. */
        MODEL_MISSING,
        /** Timed out waiting for the model. */
        TIMEOUT,
        /** The response could not be understood. */
        BAD_RESPONSE,
        /** The request was dropped because too many were in flight. */
        OVERLOADED,
        /** Anything else. */
        OTHER
    }

    private final Kind kind;

    public LlmException(Kind kind, String message) {
        super(message);
        this.kind = kind;
    }

    public LlmException(Kind kind, String message, Throwable cause) {
        super(message, cause);
        this.kind = kind;
    }

    public Kind kind() {
        return kind;
    }

    /** Short, player-safe explanation. */
    public String friendly() {
        switch (kind) {
            case UNAVAILABLE:
                return "backend is not reachable";
            case MODEL_MISSING:
                return "model is not installed";
            case TIMEOUT:
                return "the model took too long";
            case OVERLOADED:
                return "too many requests in flight";
            case BAD_RESPONSE:
                return "the model sent an unreadable answer";
            case HTTP_ERROR:
            case OTHER:
            default:
                return getMessage() == null ? "unknown error" : getMessage();
        }
    }
}
