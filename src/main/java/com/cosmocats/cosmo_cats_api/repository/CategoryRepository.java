package com.cosmocats.cosmo_cats_api.repository;

import com.cosmocats.cosmo_cats_api.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameIgnoreCase(String name);
}
