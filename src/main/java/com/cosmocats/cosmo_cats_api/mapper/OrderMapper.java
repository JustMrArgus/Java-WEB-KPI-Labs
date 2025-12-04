package com.cosmocats.cosmo_cats_api.mapper;

import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.dto.OrderRequestDto;
import com.cosmocats.cosmo_cats_api.dto.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "productIds", expression = "java(toProductIds(order.getProducts()))")
    OrderResponseDto toResponseDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    Order toDomain(OrderRequestDto requestDto);

    default List<Long> toProductIds(Set<Product> products) {
        return products == null
                ? List.of()
                : products.stream()
                .map(Product::getId)
                .toList();
    }
}