package com.cosmocats.cosmo_cats_api.web;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import com.cosmocats.cosmo_cats_api.entity.CategoryEntity;
import com.cosmocats.cosmo_cats_api.entity.ProductEntity;
import com.cosmocats.cosmo_cats_api.dto.ProductRequestDto;
import com.cosmocats.cosmo_cats_api.repository.CategoryRepository;
import com.cosmocats.cosmo_cats_api.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DisplayName("Product Controller Integration Tests")
@Tag("product-controller")
@Transactional
@WithMockUser(username = "astro-cat", roles = {"USER", "ADMIN"})
class ProductControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryEntity createTestCategory(String name) {
        return categoryRepository.save(CategoryEntity.builder()
                .name(name)
                .description("Test category description")
                .build());
    }

    private ProductEntity createTestProduct(String name, String sku, CategoryEntity category) {
        return productRepository.save(ProductEntity.builder()
                .name(name)
                .description("Test product description must be long enough")
                .price(BigDecimal.valueOf(29.99))
                .currency("USD")
                .sku(sku)
                .category(category)
                .build());
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/products - should return all products (Authorized)")
    void getAllProducts_ShouldReturnList() {
        CategoryEntity category = createTestCategory("Furniture GetAll");
        createTestProduct("Cosmic star cat bed", "COSMO-GET-ALL", category);

        mockMvc.perform(get("/api/v1/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cosmic star cat bed"));
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/products - should return 401 when Unauthorized")
    @WithAnonymousUser
    void getAllProducts_WhenUnauth_ShouldReturn401() {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/products/{id} - should return product by ID")
    void getProductById_ShouldReturnProduct() {
        CategoryEntity category = createTestCategory("Furniture GetById");
        ProductEntity savedProduct = createTestProduct("Cosmic star cat bed", "COSMO-GET-BY-ID", category);

        mockMvc.perform(get("/api/v1/products/" + savedProduct.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cosmic star cat bed"));
    }

    @Test
    @SneakyThrows
    @DisplayName("POST /api/v1/products - should create new product")
    void createProduct_ShouldReturnCreatedProduct() {
        CategoryEntity category = createTestCategory("Furniture Create");

        ProductRequestDto newProductDto = ProductRequestDto.builder()
                .name("Star Toy 3000")
                .description("Very fun laser toy for active cats")
                .price(BigDecimal.valueOf(10.50))
                .currency("USD")
                .sku("LASER-01")
                .categoryId(category.getId())
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProductDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Star Toy 3000"));
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT /api/v1/products/{id} - should update existing product")
    void updateProduct_ShouldReturnUpdatedProduct() {
        CategoryEntity category = createTestCategory("Furniture Update");
        ProductEntity savedProduct = createTestProduct("Original Bed", "COSMO-UPDATE", category);

        ProductRequestDto updateDto = ProductRequestDto.builder()
                .name("Updated Bed Super star")
                .description("Updated soft and cozy bed description")
                .price(BigDecimal.valueOf(50.00))
                .currency("USD")
                .sku("COSMO-UPDATE")
                .categoryId(category.getId())
                .build();

        mockMvc.perform(put("/api/v1/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Bed Super star"));
    }

    @Test
    @SneakyThrows
    @DisplayName("DELETE /api/v1/products/{id} - should delete product")
    void deleteProduct_ShouldReturnNoContent() {
        CategoryEntity category = createTestCategory("Furniture Delete");
        ProductEntity savedProduct = createTestProduct("To Delete Bed", "COSMO-DELETE", category);

        mockMvc.perform(delete("/api/v1/products/" + savedProduct.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/products/" + savedProduct.getId()))
                .andExpect(status().isNotFound());
    }
}