package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.exception.CategoryNotFoundException;
import com.cosmocats.cosmo_cats_api.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@DisplayName("Category Service Tests")
class CategoryServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create category")
    void shouldCreateCategory() {
        Category category = Category.builder()
                .name("Cosmic star Toys")
                .description("Toys from outer space")
                .build();

        Category created = categoryService.createCategory(category);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Cosmic star Toys");
        assertThat(created.getDescription()).isEqualTo("Toys from outer space");
    }

    @Test
    @DisplayName("Should get all categories")
    void shouldGetAllCategories() {
        categoryService.createCategory(Category.builder()
                .name("Category star 1")
                .description("Desc 1")
                .build());
        categoryService.createCategory(Category.builder()
                .name("Category star 2")
                .description("Desc 2")
                .build());

        List<Category> categories = categoryService.getAllCategories();

        assertThat(categories).hasSize(2);
    }

    @Test
    @DisplayName("Should return empty list when no categories exist")
    void shouldReturnEmptyListWhenNoCategoriesExist() {
        List<Category> categories = categoryService.getAllCategories();

        assertThat(categories).isEmpty();
    }

    @Test
    @DisplayName("Should get category by id")
    void shouldGetCategoryById() {
        Category created = categoryService.createCategory(Category.builder()
                .name("Galaxy star Food")
                .description("Food from the galaxy")
                .build());

        Optional<Category> found = categoryService.getCategoryById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Galaxy star Food");
    }

    @Test
    @DisplayName("Should return empty when category not found by id")
    void shouldReturnEmptyWhenCategoryNotFoundById() {
        Optional<Category> found = categoryService.getCategoryById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should update category")
    void shouldUpdateCategory() {
        Category created = categoryService.createCategory(Category.builder()
                .name("Old star Name")
                .description("Old Description")
                .build());

        Category updateData = Category.builder()
                .name("New star Name")
                .description("New Description")
                .build();

        Category updated = categoryService.updateCategory(created.getId(), updateData);

        assertThat(updated.getName()).isEqualTo("New star Name");
        assertThat(updated.getDescription()).isEqualTo("New Description");
        assertThat(updated.getId()).isEqualTo(created.getId());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent category")
    void shouldThrowExceptionWhenUpdatingNonExistentCategory() {
        Category updateData = Category.builder()
                .name("New Name")
                .description("New Description")
                .build();

        assertThatThrownBy(() -> categoryService.updateCategory(999L, updateData))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("Should delete category")
    void shouldDeleteCategory() {
        Category created = categoryService.createCategory(Category.builder()
                .name("To Be star Deleted")
                .description("Will be deleted")
                .build());

        categoryService.deleteCategory(created.getId());

        assertThat(categoryService.getCategoryById(created.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent category")
    void shouldThrowExceptionWhenDeletingNonExistentCategory() {
        assertThatThrownBy(() -> categoryService.deleteCategory(999L))
                .isInstanceOf(CategoryNotFoundException.class);
    }
}
