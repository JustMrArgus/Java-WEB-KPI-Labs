package com.cosmocats.cosmo_cats_api.mapper;

import org.mapstruct.Mapper;
import com.cosmocats.cosmo_cats_api.domain.Cart;
import com.cosmocats.cosmo_cats_api.dto.CartDto;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toCartDto(Cart cart);
    Cart toCartEntity(CartDto dto);
}