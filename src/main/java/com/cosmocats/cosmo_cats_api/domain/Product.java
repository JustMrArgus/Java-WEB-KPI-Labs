package com.cosmocats.cosmo_cats_api.domain;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(exclude = "category")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String currency;

    private String sku;

    private Category category;
}
