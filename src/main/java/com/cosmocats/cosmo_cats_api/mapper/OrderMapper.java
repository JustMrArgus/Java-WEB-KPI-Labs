package com.cosmocats.cosmo_cats_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.dto.OrderDto;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDto toDto(Order order);
    Order toEntity(OrderDto dto);
}
