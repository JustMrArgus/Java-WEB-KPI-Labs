package com.cosmocats.cosmo_cats_api.mapper;

import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.dto.OrderDto;
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
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDto toOrderDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setId( order.getId() );
        orderDto.setCreatedAt( order.getCreatedAt() );
        orderDto.setStatus( order.getStatus() );
        List<Long> list = order.getProductIds();
        if ( list != null ) {
            orderDto.setProductIds( new ArrayList<Long>( list ) );
        }

        return orderDto;
    }

    @Override
    public Order toOrderEntity(OrderDto dto) {
        if ( dto == null ) {
            return null;
        }

        Order order = new Order();

        order.setId( dto.getId() );
        order.setCreatedAt( dto.getCreatedAt() );
        order.setStatus( dto.getStatus() );
        List<Long> list = dto.getProductIds();
        if ( list != null ) {
            order.setProductIds( new ArrayList<Long>( list ) );
        }

        return order;
    }
}
