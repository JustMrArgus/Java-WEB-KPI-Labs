package com.cosmocats.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String currency;
    private String sku;
    private Long categoryId;
}
