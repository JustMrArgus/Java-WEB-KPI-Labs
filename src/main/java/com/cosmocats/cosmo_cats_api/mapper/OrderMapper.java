package com.cosmocats.cosmo_cats_api.mapper;

import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "productIds", expression = "java(toProductIds(order.getProducts()))")
    OrderDto toOrderDto(Order order);

    @Mapping(target = "products", ignore = true)
    Order toOrderEntity(OrderDto dto);

    default List<Long> toProductIds(Set<Product> products) {
        return products == null
                ? List.of()
                : products.stream()
                .map(Product::getId)
                .toList();
    }

}