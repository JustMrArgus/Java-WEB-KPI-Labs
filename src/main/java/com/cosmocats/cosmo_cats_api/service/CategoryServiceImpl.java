package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.entity.CategoryEntity;
import com.cosmocats.cosmo_cats_api.exception.CategoryNotFoundException;
import com.cosmocats.cosmo_cats_api.mapper.CategoryEntityMapper;
import com.cosmocats.cosmo_cats_api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        CategoryEntity entity = categoryEntityMapper.toEntity(category);
        CategoryEntity saved = categoryRepository.save(entity);
        return categoryEntityMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryEntityMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, Category category) {
        CategoryEntity existing = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        CategoryEntity saved = categoryRepository.save(existing);
        return categoryEntityMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }
}
