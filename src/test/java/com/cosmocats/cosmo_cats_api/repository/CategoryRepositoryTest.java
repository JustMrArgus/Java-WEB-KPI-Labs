package com.cosmocats.cosmo_cats_api.repository;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import com.cosmocats.cosmo_cats_api.entity.CategoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CategoryRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should find category by name ignoring case")
    void shouldFindByNameIgnoreCase() {
        CategoryEntity category = CategoryEntity.builder()
                .name("Wet star Food")
                .description("Delicious")
                .build();
        categoryRepository.save(category);

        Optional<CategoryEntity> found = categoryRepository.findByNameIgnoreCase("wet star food");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Wet star Food");
    }
}