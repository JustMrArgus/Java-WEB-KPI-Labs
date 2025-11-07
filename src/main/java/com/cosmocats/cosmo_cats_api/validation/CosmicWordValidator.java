package com.cosmocats.cosmo_cats_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordCheck, String> {

    private static final List<String> cosmicWords = List.of("star", "galaxy", "comet", "planet", "universe");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return cosmicWords.stream().anyMatch(word -> value.toLowerCase().contains(word));
    }
}