package com.cosmocats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cosmocats.domain.Product;
import com.cosmocats.dto.ProductDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product product);
    Product toEntity(ProductDto dto);
}
