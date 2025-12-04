package com.cosmocats.cosmo_cats_api.exception;

public class PersistenceException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "A database error occurred";

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
