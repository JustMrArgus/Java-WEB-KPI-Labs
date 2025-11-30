package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.exception.OrderNotFoundException;
import com.cosmocats.cosmo_cats_api.exception.ProductNotFoundException;
import com.cosmocats.cosmo_cats_api.repository.CategoryRepository;
import com.cosmocats.cosmo_cats_api.repository.OrderRepository;
import com.cosmocats.cosmo_cats_api.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@DisplayName("Order Service Tests")
class OrderServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product savedProduct1;
    private Product savedProduct2;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        Category category = categoryRepository.save(
                Category.builder()
                        .name("Test star Category")
                        .description("Test Description")
                        .build()
        );

        savedProduct1 = productRepository.save(
                Product.builder()
                        .name("Cosmic star Toy 1")
                        .description("A cosmic toy")
                        .price(BigDecimal.valueOf(25.99))
                        .currency("USD")
                        .sku("SKU-001")
                        .category(category)
                        .build()
        );

        savedProduct2 = productRepository.save(
                Product.builder()
                        .name("Cosmic star Toy 2")
                        .description("Another cosmic toy")
                        .price(BigDecimal.valueOf(35.99))
                        .currency("USD")
                        .sku("SKU-002")
                        .category(category)
                        .build()
        );
    }

    @Test
    @DisplayName("Should create order with products")
    void shouldCreateOrderWithProducts() {
        Order order = Order.builder()
                .customerEmail("cat@cosmos.com")
                .status("NEW")
                .build();

        Order created = orderService.createOrder(order, List.of(savedProduct1.getId()));

        assertThat(created.getId()).isNotNull();
        assertThat(created.getOrderNumber()).isNotNull();
        assertThat(created.getCustomerEmail()).isEqualTo("cat@cosmos.com");
        assertThat(created.getProducts()).hasSize(1);
    }

    @Test
    @DisplayName("Should create order with provided order number")
    void shouldCreateOrderWithProvidedOrderNumber() {
        Order order = Order.builder()
                .orderNumber("CUSTOM-ORDER-123")
                .customerEmail("cat2@cosmos.com")
                .status("NEW")
                .build();

        Order created = orderService.createOrder(order, List.of(savedProduct1.getId()));

        assertThat(created.getOrderNumber()).isEqualTo("CUSTOM-ORDER-123");
    }

    @Test
    @DisplayName("Should create order with multiple products")
    void shouldCreateOrderWithMultipleProducts() {
        Order order = Order.builder()
                .customerEmail("cat3@cosmos.com")
                .status("NEW")
                .build();

        Order created = orderService.createOrder(order, List.of(savedProduct1.getId(), savedProduct2.getId()));

        assertThat(created.getProducts()).hasSize(2);
    }

    @Test
    @DisplayName("Should throw exception when creating order with non-existent product")
    void shouldThrowExceptionWhenCreatingOrderWithNonExistentProduct() {
        Order order = Order.builder()
                .customerEmail("cat@cosmos.com")
                .status("NEW")
                .build();

        assertThatThrownBy(() -> orderService.createOrder(order, List.of(999L)))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw exception when creating order with empty product list")
    void shouldThrowExceptionWhenCreatingOrderWithEmptyProductList() {
        Order order = Order.builder()
                .customerEmail("cat@cosmos.com")
                .status("NEW")
                .build();

        assertThatThrownBy(() -> orderService.createOrder(order, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one product");
    }

    @Test
    @DisplayName("Should throw exception when creating order with null product list")
    void shouldThrowExceptionWhenCreatingOrderWithNullProductList() {
        Order order = Order.builder()
                .customerEmail("cat@cosmos.com")
                .status("NEW")
                .build();

        assertThatThrownBy(() -> orderService.createOrder(order, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should get all orders")
    void shouldGetAllOrders() {
        Order order1 = Order.builder()
                .customerEmail("cat1@cosmos.com")
                .status("NEW")
                .build();
        Order order2 = Order.builder()
                .customerEmail("cat2@cosmos.com")
                .status("NEW")
                .build();

        orderService.createOrder(order1, List.of(savedProduct1.getId()));
        orderService.createOrder(order2, List.of(savedProduct2.getId()));

        List<Order> orders = orderService.getAllOrders();

        assertThat(orders).hasSize(2);
    }

    @Test
    @DisplayName("Should get order by id")
    void shouldGetOrderById() {
        Order order = Order.builder()
                .customerEmail("cat@cosmos.com")
                .status("NEW")
                .build();
        Order created = orderService.createOrder(order, List.of(savedProduct1.getId()));

        Optional<Order> found = orderService.getOrderById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCustomerEmail()).isEqualTo("cat@cosmos.com");
    }

    @Test
    @DisplayName("Should return empty when order not found by id")
    void shouldReturnEmptyWhenOrderNotFoundById() {
        Optional<Order> found = orderService.getOrderById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should get order by order number")
    void shouldGetOrderByOrderNumber() {
        Order order = Order.builder()
                .orderNumber("TEST-ORDER-001")
                .customerEmail("cat@cosmos.com")
                .status("NEW")
                .build();
        orderService.createOrder(order, List.of(savedProduct1.getId()));

        Optional<Order> found = orderService.getOrderByOrderNumber("TEST-ORDER-001");

        assertThat(found).isPresent();
        assertThat(found.get().getCustomerEmail()).isEqualTo("cat@cosmos.com");
    }

    @Test
    @DisplayName("Should return empty when order not found by order number")
    void shouldReturnEmptyWhenOrderNotFoundByOrderNumber() {
        Optional<Order> found = orderService.getOrderByOrderNumber("NON-EXISTENT");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should update order")
    void shouldUpdateOrder() {
        Order order = Order.builder()
                .customerEmail("old@cosmos.com")
                .status("NEW")
                .build();
        Order created = orderService.createOrder(order, List.of(savedProduct1.getId()));

        Order updateData = Order.builder()
                .customerEmail("new@cosmos.com")
                .status("COMPLETED")
                .build();

        Order updated = orderService.updateOrder(created.getId(), updateData, null);

        assertThat(updated.getCustomerEmail()).isEqualTo("new@cosmos.com");
        assertThat(updated.getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    @DisplayName("Should update order with new products")
    void shouldUpdateOrderWithNewProducts() {
        Order order = Order.builder()
                .customerEmail("cat@cosmos.com")
                .status("NEW")
                .build();
        Order created = orderService.createOrder(order, List.of(savedProduct1.getId()));

        Order updateData = Order.builder()
                .customerEmail("cat@cosmos.com")
                .status("PROCESSING")
                .build();

        Order updated = orderService.updateOrder(created.getId(), updateData, List.of(savedProduct2.getId()));

        assertThat(updated.getProducts()).hasSize(1);
        assertThat(updated.getProducts().iterator().next().getId()).isEqualTo(savedProduct2.getId());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent order")
    void shouldThrowExceptionWhenUpdatingNonExistentOrder() {
        Order updateData = Order.builder()
                .customerEmail("cat@cosmos.com")
                .status("NEW")
                .build();

        assertThatThrownBy(() -> orderService.updateOrder(999L, updateData, null))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("Should delete order")
    void shouldDeleteOrder() {
        Order order = Order.builder()
                .customerEmail("cat@cosmos.com")
                .status("NEW")
                .build();
        Order created = orderService.createOrder(order, List.of(savedProduct1.getId()));

        orderService.deleteOrder(created.getId());

        assertThat(orderService.getOrderById(created.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent order")
    void shouldThrowExceptionWhenDeletingNonExistentOrder() {
        assertThatThrownBy(() -> orderService.deleteOrder(999L))
                .isInstanceOf(OrderNotFoundException.class);
    }
}
