package com.cosmocats.cosmo_cats_api.controller;

import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.dto.OrderDto;
import com.cosmocats.cosmo_cats_api.exception.OrderNotFoundException;
import com.cosmocats.cosmo_cats_api.mapper.OrderMapper;
import com.cosmocats.cosmo_cats_api.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Valid @RequestBody OrderDto dto) {
        Order order = orderMapper.toOrderEntity(dto);
        return orderMapper.toOrderDto(orderService.createOrder(order, dto.getProductIds()));
    }

    @GetMapping
    public List<OrderDto> readAll() {
        return orderService.getAllOrders().stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @GetMapping("/{id}")
    public OrderDto readById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(orderMapper::toOrderDto)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @GetMapping("/number/{orderNumber}")
    public OrderDto readByOrderNumber(@PathVariable String orderNumber) {
        return orderService.getOrderByOrderNumber(orderNumber)
                .map(orderMapper::toOrderDto)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }

    @PutMapping("/{id}")
    public OrderDto update(@PathVariable Long id, @Valid @RequestBody OrderDto dto) {
        Order order = orderMapper.toOrderEntity(dto);
        return orderMapper.toOrderDto(orderService.updateOrder(id, order, dto.getProductIds()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
