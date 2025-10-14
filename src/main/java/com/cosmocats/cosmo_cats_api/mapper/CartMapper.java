package com.cosmocats.cosmo_cats_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cosmocats.cosmo_cats_api.domain.Cart;
import com.cosmocats.cosmo_cats_api.dto.CartDto;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartDto toDto(Cart cart);
    Cart toEntity(CartDto dto);
}
