package com.cosmocats.cosmo_cats_api.mapper;

import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryEntityMapper.class})
public interface ProductEntityMapper {

    @Mapping(target = "category", source = "category")
    Product toDomain(ProductEntity entity);

    @Mapping(target = "category", source = "category")
    ProductEntity toEntity(Product domain);
}
