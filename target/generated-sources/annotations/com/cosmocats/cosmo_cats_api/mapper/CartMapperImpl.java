package com.cosmocats.cosmo_cats_api.mapper;

import com.cosmocats.cosmo_cats_api.domain.Cart;
import com.cosmocats.cosmo_cats_api.dto.CartDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-25T03:51:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Red Hat, Inc.)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartDto toCartDto(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartDto cartDto = new CartDto();

        cartDto.setId( cart.getId() );
        cartDto.setSessionId( cart.getSessionId() );
        List<Long> list = cart.getProductIds();
        if ( list != null ) {
            cartDto.setProductIds( new ArrayList<Long>( list ) );
        }

        return cartDto;
    }

    @Override
    public Cart toCartEntity(CartDto dto) {
        if ( dto == null ) {
            return null;
        }

        Cart cart = new Cart();

        cart.setId( dto.getId() );
        cart.setSessionId( dto.getSessionId() );
        List<Long> list = dto.getProductIds();
        if ( list != null ) {
            cart.setProductIds( new ArrayList<Long>( list ) );
        }

        return cart;
    }
}
