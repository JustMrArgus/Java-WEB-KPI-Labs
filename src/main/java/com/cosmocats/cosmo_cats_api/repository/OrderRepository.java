package com.cosmocats.cosmo_cats_api.repository;

import com.cosmocats.cosmo_cats_api.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
}
