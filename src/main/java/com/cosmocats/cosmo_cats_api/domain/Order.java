package com.cosmocats.cosmo_cats_api.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "products")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @EqualsAndHashCode.Include
    private Long id;

    private String orderNumber;

    private LocalDateTime createdAt;

    private String status;

    private String customerEmail;

    @Builder.Default
    private Set<Product> products = new HashSet<>();
}
