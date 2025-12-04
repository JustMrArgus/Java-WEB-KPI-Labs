package com.cosmocats.cosmo_cats_api.exception;

public class DataIntegrityException extends PersistenceException {

    private static final String MESSAGE_TEMPLATE = "Data integrity violation: %s";

    public DataIntegrityException(String detail) {
        super(String.format(MESSAGE_TEMPLATE, detail));
    }

    public DataIntegrityException(String detail, Throwable cause) {
        super(String.format(MESSAGE_TEMPLATE, detail), cause);
    }
}
