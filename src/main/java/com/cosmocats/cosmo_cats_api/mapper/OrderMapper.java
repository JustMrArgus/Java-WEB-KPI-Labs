package com.cosmocats.cosmo_cats_api.mapper;

import org.mapstruct.Mapper;
import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.dto.OrderDto;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toOrderDto(Order order);
    Order toOrderEntity(OrderDto dto);
}