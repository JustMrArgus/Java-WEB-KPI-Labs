package com.cosmocats.cosmo_cats_api.mapper;

import org.mapstruct.Mapper;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.dto.ProductDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toProductDto(Product product);
    Product toProductEntity(ProductDto dto);
}
