package com.cosmocats.cosmo_cats_api.mapper;

import org.mapstruct.Mapper;
import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.dto.CategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);
    Category toCategoryEntity(CategoryDto dto);
}