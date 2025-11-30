package com.cosmocats.cosmo_cats_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {
    private Long id;

    @NotBlank
    private String name;

    private String description;
}