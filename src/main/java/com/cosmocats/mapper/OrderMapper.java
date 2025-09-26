package com.cosmocats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cosmocats.domain.Order;
import com.cosmocats.dto.OrderDto;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDto toDto(Order order);
    Order toEntity(OrderDto dto);
}
