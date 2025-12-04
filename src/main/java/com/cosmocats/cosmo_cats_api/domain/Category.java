package com.cosmocats.cosmo_cats_api.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "products")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private String description;

    @Builder.Default
    private List<Product> products = new ArrayList<>();
}
