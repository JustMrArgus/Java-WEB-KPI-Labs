package com.cosmocats.cosmo_cats_api.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

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
@Entity
@Table(name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_orders_order_number", columnNames = "order_number"),
                @UniqueConstraint(name = "uq_orders_customer_email", columnNames = "customer_email")
        },
        indexes = {
                @Index(name = "idx_orders_created_at", columnList = "created_at")
        })
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_seq_gen")
    @SequenceGenerator(name = "orders_id_seq_gen", sequenceName = "orders_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @NaturalId
    @Column(name = "order_number", nullable = false, length = 64)
    private String orderNumber;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 32)
    private String status;

    @Column(name = "customer_email", nullable = false, length = 255)
    private String customerEmail;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_products_order")),
            inverseJoinColumns = @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_order_products_product")))
    private Set<Product> products = new HashSet<>();

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
