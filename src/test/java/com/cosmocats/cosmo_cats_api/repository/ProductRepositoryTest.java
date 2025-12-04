package com.cosmocats.cosmo_cats_api.repository;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import com.cosmocats.cosmo_cats_api.entity.CategoryEntity;
import com.cosmocats.cosmo_cats_api.entity.OrderEntity;
import com.cosmocats.cosmo_cats_api.entity.ProductEntity;
import com.cosmocats.cosmo_cats_api.repository.projection.ProductSalesProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ProductRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderRepository orderRepository;

    private CategoryEntity createTestCategory(String name) {
        return categoryRepository.save(
                CategoryEntity.builder()
                        .name(name)
                        .description("Toys from outer space")
                        .build()
        );
    }

    @Test
    @DisplayName("Should save and return product with ID")
    void shouldSaveProduct() {
        CategoryEntity category = createTestCategory("Cosmic Toys 1");
        ProductEntity product = ProductEntity.builder()
                .name("Moon star Laser")
                .description("Pew Pew Laser")
                .price(new BigDecimal("100.00"))
                .currency("USD")
                .sku("SKU-100")
                .category(category)
                .build();

        ProductEntity saved = productRepository.save(product);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Moon star Laser");
    }

    @Test
    @DisplayName("Should find product by SKU")
    void shouldFindProductBySku() {
        CategoryEntity category = createTestCategory("Cosmic Toys 2");
        ProductEntity product = ProductEntity.builder()
                .name("Mars star Ball")
                .price(BigDecimal.TEN)
                .currency("EUR")
                .sku("SKU-MARS")
                .category(category)
                .build();
        productRepository.save(product);

        Optional<ProductEntity> found = productRepository.findBySku("SKU-MARS");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mars star Ball");
    }

    @Test
    @DisplayName("Should find top products by sales (Projection Test)")
    void shouldFindTopProductsByStatus() {
        CategoryEntity category = createTestCategory("Cosmic Toys 3");
        ProductEntity p1 = productRepository.save(ProductEntity.builder().name("Hit Product").price(BigDecimal.TEN).currency("USD").sku("P1").category(category).build());
        ProductEntity p2 = productRepository.save(ProductEntity.builder().name("Low Product").price(BigDecimal.TEN).currency("USD").sku("P2").category(category).build());

        OrderEntity o1 = OrderEntity.builder().orderNumber("ORD-1").customerEmail("1@test.com").status("COMPLETED").build();
        o1.setProducts(Set.of(p1));

        OrderEntity o2 = OrderEntity.builder().orderNumber("ORD-2").customerEmail("2@test.com").status("COMPLETED").build();
        o2.setProducts(Set.of(p1, p2));

        orderRepository.saveAll(List.of(o1, o2));

        List<ProductSalesProjection> result = productRepository.findTopProductsByStatus("COMPLETED", Pageable.unpaged());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getProductName()).isEqualTo("Hit Product");
        assertThat(result.get(0).getOrderCount()).isEqualTo(2L);
    }
}