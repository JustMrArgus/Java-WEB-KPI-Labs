package com.cosmocats.cosmo_cats_api.exception;

public class FeatureNotAvailableException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Feature '%s' is not currently available.";

    public FeatureNotAvailableException(String featureName) {
        super(String.format(ERROR_MESSAGE, featureName));
    }
}