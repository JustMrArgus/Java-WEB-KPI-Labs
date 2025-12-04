package com.cosmocats.cosmo_cats_api.repository;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import com.cosmocats.cosmo_cats_api.entity.OrderEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class OrderRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Should find order by Order Number")
    void shouldFindByOrderNumber() {
        OrderEntity order = OrderEntity.builder()
                .orderNumber("ORD-999-XYZ")
                .customerEmail("spacecat@mail.com")
                .status("NEW")
                .build();

        orderRepository.save(order);

        Optional<OrderEntity> found = orderRepository.findByOrderNumber("ORD-999-XYZ");

        assertThat(found).isPresent();
        assertThat(found.get().getCustomerEmail()).isEqualTo("spacecat@mail.com");
    }
}