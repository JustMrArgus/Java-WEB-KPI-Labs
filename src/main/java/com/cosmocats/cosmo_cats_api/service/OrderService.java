package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(Order order, List<Long> productIds);

    List<Order> getAllOrders();

    Optional<Order> getOrderById(Long id);

    Optional<Order> getOrderByOrderNumber(String orderNumber);

    Order updateOrder(Long id, Order order, List<Long> productIds);

    void deleteOrder(Long id);
}
