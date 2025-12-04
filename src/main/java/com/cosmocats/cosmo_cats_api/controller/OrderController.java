package com.cosmocats.cosmo_cats_api.controller;

import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.dto.OrderRequestDto;
import com.cosmocats.cosmo_cats_api.dto.OrderResponseDto;
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
    public OrderResponseDto create(@Valid @RequestBody OrderRequestDto requestDto) {
        Order order = orderMapper.toDomain(requestDto);
        return orderMapper.toResponseDto(orderService.createOrder(order, requestDto.getProductIds()));
    }

    @GetMapping
    public List<OrderResponseDto> readAll() {
        return orderService.getAllOrders().stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @GetMapping("/{id}")
    public OrderResponseDto readById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(orderMapper::toResponseDto)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @GetMapping("/number/{orderNumber}")
    public OrderResponseDto readByOrderNumber(@PathVariable String orderNumber) {
        return orderService.getOrderByOrderNumber(orderNumber)
                .map(orderMapper::toResponseDto)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }

    @PutMapping("/{id}")
    public OrderResponseDto update(@PathVariable Long id, @Valid @RequestBody OrderRequestDto requestDto) {
        Order order = orderMapper.toDomain(requestDto);
        return orderMapper.toResponseDto(orderService.updateOrder(id, order, requestDto.getProductIds()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
