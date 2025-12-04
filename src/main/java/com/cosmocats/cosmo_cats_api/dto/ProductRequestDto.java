package com.cosmocats.cosmo_cats_api.dto;

import com.cosmocats.cosmo_cats_api.validation.CosmicWordCheck;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Name cannot be empty")
    @CosmicWordCheck
    private String name;

    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.1", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Currency cannot be empty")
    private String currency;

    @NotBlank(message = "SKU cannot be empty")
    private String sku;

    @NotNull(message = "CategoryId is required")
    private Long categoryId;
}
