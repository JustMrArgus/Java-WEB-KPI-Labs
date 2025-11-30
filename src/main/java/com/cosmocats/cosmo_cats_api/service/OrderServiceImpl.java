package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.exception.OrderNotFoundException;
import com.cosmocats.cosmo_cats_api.exception.ProductNotFoundException;
import com.cosmocats.cosmo_cats_api.repository.OrderRepository;
import com.cosmocats.cosmo_cats_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final DateTimeFormatter ORDER_NUMBER_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Order createOrder(Order order, List<Long> productIds) {
        order.setProducts(resolveProducts(productIds));
        order.setOrderNumber(generateOrderNumber(order.getOrderNumber()));
        return hydrate(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(this::hydrate);
        return orders;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::hydrate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber).map(this::hydrate);
    }

    @Override
    public Order updateOrder(Long id, Order order, List<Long> productIds) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        existing.setStatus(order.getStatus());
        existing.setCustomerEmail(order.getCustomerEmail());
        if (order.getCreatedAt() != null) {
            existing.setCreatedAt(order.getCreatedAt());
        }
        if (productIds != null && !productIds.isEmpty()) {
            existing.setProducts(resolveProducts(productIds));
        }
        return hydrate(orderRepository.save(existing));
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
    }

    private Set<Product> resolveProducts(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product id");
        }
        Set<Product> products = new HashSet<>(productRepository.findAllById(productIds));
        if (products.size() != new HashSet<>(productIds).size()) {
            Set<Long> missing = new HashSet<>(productIds);
            products.forEach(product -> missing.remove(product.getId()));
            Long missingId = missing.stream().findFirst().orElse(-1L);
            throw new ProductNotFoundException(missingId);
        }
        return products;
    }

    private String generateOrderNumber(String provided) {
        if (provided != null && !provided.isBlank()) {
            return provided;
        }
        String timePart = ORDER_NUMBER_FORMATTER.format(LocalDateTime.now());
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + timePart + '-' + randomPart;
    }

    private Order hydrate(Order order) {
        if (order != null && order.getProducts() != null) {
            order.getProducts().size();
        }
        return order;
    }
}
