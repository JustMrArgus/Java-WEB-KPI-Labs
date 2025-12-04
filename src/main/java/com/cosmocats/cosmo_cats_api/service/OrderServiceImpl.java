package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.entity.OrderEntity;
import com.cosmocats.cosmo_cats_api.entity.ProductEntity;
import com.cosmocats.cosmo_cats_api.exception.OrderNotFoundException;
import com.cosmocats.cosmo_cats_api.exception.ProductNotFoundException;
import com.cosmocats.cosmo_cats_api.mapper.OrderEntityMapper;
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
public class OrderServiceImpl implements OrderService {

    private static final DateTimeFormatter ORDER_NUMBER_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderEntityMapper orderEntityMapper;

    @Override
    @Transactional
    public Order createOrder(Order order, List<Long> productIds) {
        OrderEntity entity = orderEntityMapper.toEntity(order);
        entity.setProducts(resolveProducts(productIds));
        entity.setOrderNumber(generateOrderNumber(order.getOrderNumber()));
        OrderEntity saved = orderRepository.save(entity);
        return hydrate(orderEntityMapper.toDomain(saved));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderEntityMapper::toDomain)
                .map(this::hydrate)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderEntityMapper::toDomain)
                .map(this::hydrate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(orderEntityMapper::toDomain)
                .map(this::hydrate);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order order, List<Long> productIds) {
        OrderEntity existing = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        existing.setStatus(order.getStatus());
        existing.setCustomerEmail(order.getCustomerEmail());
        if (order.getCreatedAt() != null) {
            existing.setCreatedAt(order.getCreatedAt());
        }
        if (productIds != null && !productIds.isEmpty()) {
            existing.setProducts(resolveProducts(productIds));
        }
        OrderEntity saved = orderRepository.save(existing);
        return hydrate(orderEntityMapper.toDomain(saved));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
    }

    private Set<ProductEntity> resolveProducts(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product id");
        }
        Set<ProductEntity> products = new HashSet<>(productRepository.findAllById(productIds));
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
        return String.format("ORD-%s-%s", timePart, randomPart);
    }

    private Order hydrate(Order order) {
        if (order != null && order.getProducts() != null) {
            order.getProducts().size();
        }
        return order;
    }
}
