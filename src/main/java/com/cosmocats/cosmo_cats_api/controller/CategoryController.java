package com.cosmocats.cosmo_cats_api.controller;

import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.dto.CategoryDto;
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
    public CategoryDto create(@Valid @RequestBody CategoryDto dto) {
        Category category = categoryMapper.toCategoryEntity(dto);
        return categoryMapper.toCategoryDto(categoryService.createCategory(category));
    }

    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.getAllCategories().stream()
                .map(categoryMapper::toCategoryDto)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(categoryMapper::toCategoryDto)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable Long id, @Valid @RequestBody CategoryDto dto) {
        Category category = categoryMapper.toCategoryEntity(dto);
        return categoryMapper.toCategoryDto(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
