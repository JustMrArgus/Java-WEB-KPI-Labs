package com.cosmocats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cosmocats.domain.Category;
import com.cosmocats.dto.CategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto dto);
}
