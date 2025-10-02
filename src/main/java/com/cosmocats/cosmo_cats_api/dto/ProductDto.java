package com.cosmocats.cosmo_cats_api.dto;

import com.cosmocats.cosmo_cats_api.validation.CosmicWordCheck;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ProductDto {
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @CosmicWordCheck
    private String name;

    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.1", message = "Price must be greater than 0")
    private Double price;

    @NotBlank(message = "Currency cannot be empty")
    private String currency;

    @NotBlank(message = "SKU cannot be empty")
    private String sku;

    @NotNull(message = "CategoryId is required")
    private Long categoryId;
}
