package com.cosmocats.cosmo_cats_api.controller;

import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.dto.CategoryRequestDto;
import com.cosmocats.cosmo_cats_api.dto.CategoryResponseDto;
import com.cosmocats.cosmo_cats_api.exception.CategoryNotFoundException;
import com.cosmocats.cosmo_cats_api.mapper.CategoryMapper;
import com.cosmocats.cosmo_cats_api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@Valid @RequestBody CategoryRequestDto requestDto) {
        Category category = categoryMapper.toDomain(requestDto);
        return categoryMapper.toResponseDto(categoryService.createCategory(category));
    }

    @GetMapping
    public List<CategoryResponseDto> findAll() {
        return categoryService.getAllCategories().stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryResponseDto findById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(categoryMapper::toResponseDto)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @PutMapping("/{id}")
    public CategoryResponseDto update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto requestDto) {
        Category category = categoryMapper.toDomain(requestDto);
        return categoryMapper.toResponseDto(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
