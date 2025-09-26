package com.cosmocats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cosmocats.domain.Cart;
import com.cosmocats.dto.CartDto;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartDto toDto(Cart cart);
    Cart toEntity(CartDto dto);
}
