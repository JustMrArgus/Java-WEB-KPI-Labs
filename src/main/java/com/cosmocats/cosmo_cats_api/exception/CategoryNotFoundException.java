package com.cosmocats.cosmo_cats_api.exception;

public class CategoryNotFoundException extends ResourceNotFoundException {

    private static final String MESSAGE = "Category not found with id: %s";

    public CategoryNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
