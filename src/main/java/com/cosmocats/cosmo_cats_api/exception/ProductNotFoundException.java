package com.cosmocats.cosmo_cats_api.exception;

public class ProductNotFoundException extends ResourceNotFoundException {

    private static final String ERROR_MESSAGE = "Product not found with id: %d";

    public ProductNotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}