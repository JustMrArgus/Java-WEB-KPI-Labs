package com.cosmocats.cosmo_cats_api.repository;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.repository.projection.ProductSalesProjection;
import org.junit.jupiter.api.BeforeEach;
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

    private Category savedCategory;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        Category category = Category.builder()
                .name("Cosmic Toys")
                .description("Toys from outer space")
                .build();
        savedCategory = categoryRepository.save(category);
    }

    @Test
    @DisplayName("Should save and return product with ID")
    void shouldSaveProduct() {
        Product product = Product.builder()
                .name("Moon star Laser")
                .description("Pew Pew Laser")
                .price(new BigDecimal("100.00"))
                .currency("USD")
                .sku("SKU-100")
                .category(savedCategory)
                .build();

        Product saved = productRepository.save(product);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Moon star Laser");
    }

    @Test
    @DisplayName("Should find product by SKU")
    void shouldFindProductBySku() {
        Product product = Product.builder()
                .name("Mars star Ball")
                .price(BigDecimal.TEN)
                .currency("EUR")
                .sku("SKU-MARS")
                .category(savedCategory)
                .build();
        productRepository.save(product);

        Optional<Product> found = productRepository.findBySku("SKU-MARS");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mars star Ball");
    }

    @Test
    @DisplayName("Should find top products by sales (Projection Test)")
    void shouldFindTopProductsByStatus() {
        Product p1 = productRepository.save(Product.builder().name("Hit Product").price(BigDecimal.TEN).currency("USD").sku("P1").category(savedCategory).build());
        Product p2 = productRepository.save(Product.builder().name("Low Product").price(BigDecimal.TEN).currency("USD").sku("P2").category(savedCategory).build());

        Order o1 = Order.builder().orderNumber("ORD-1").customerEmail("1@test.com").status("COMPLETED").build();
        o1.setProducts(Set.of(p1));

        Order o2 = Order.builder().orderNumber("ORD-2").customerEmail("2@test.com").status("COMPLETED").build();
        o2.setProducts(Set.of(p1, p2));

        orderRepository.saveAll(List.of(o1, o2));

        List<ProductSalesProjection> result = productRepository.findTopProductsByStatus("COMPLETED", Pageable.unpaged());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getProductName()).isEqualTo("Hit Product");
        assertThat(result.get(0).getOrderCount()).isEqualTo(2L);
    }
}