package com.cosmocats.cosmo_cats_api.repository;

import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.repository.projection.ProductSalesProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    @Query("""
            SELECT p.id AS productId,
                   p.name AS productName,
                   COUNT(o.id) AS orderCount
            FROM com.cosmocats.cosmo_cats_api.domain.Order o
            JOIN o.products p
            WHERE o.status = :status
            GROUP BY p.id, p.name
            ORDER BY COUNT(o.id) DESC
            """)
    List<ProductSalesProjection> findTopProductsByStatus(@Param("status") String status, Pageable pageable);
}
