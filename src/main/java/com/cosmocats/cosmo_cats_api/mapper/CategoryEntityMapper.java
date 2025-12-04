package com.cosmocats.cosmo_cats_api.mapper;

import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {

    @Mapping(target = "products", ignore = true)
    Category toDomain(CategoryEntity entity);

    @Mapping(target = "products", ignore = true)
    CategoryEntity toEntity(Category domain);
}
