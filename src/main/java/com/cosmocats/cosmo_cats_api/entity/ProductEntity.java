package com.cosmocats.cosmo_cats_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(exclude = "category")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_products_category_name", columnNames = {"category_id", "product_name"})
    },
    indexes = {
        @Index(name = "idx_products_name", columnList = "product_name")
    })
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_id_seq_gen")
    @SequenceGenerator(name = "products_id_seq_gen", sequenceName = "products_id_seq", allocationSize = 50)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "product_name", nullable = false, length = 150)
    private String name;

    @Column(length = 512)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, unique = true, length = 64)
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_products_category"))
    private CategoryEntity category;
}
