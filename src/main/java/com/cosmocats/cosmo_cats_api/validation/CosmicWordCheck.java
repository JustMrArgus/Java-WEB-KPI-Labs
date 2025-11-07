package com.cosmocats.cosmo_cats_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CosmicWordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmicWordCheck {
    String message() default "Product name must contain a cosmic term (e.g., star, galaxy, comet)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
