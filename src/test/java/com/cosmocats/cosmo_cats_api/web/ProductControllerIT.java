package com.cosmocats.cosmo_cats_api.web;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.dto.ProductDto;
import com.cosmocats.cosmo_cats_api.repository.CategoryRepository;
import com.cosmocats.cosmo_cats_api.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // Імпорт для виводу логів
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DisplayName("Product Controller Integration Tests")
@Tag("product-controller")
@Transactional
class ProductControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product savedProduct;
    private Category savedCategory;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        savedCategory = categoryRepository.save(Category.builder()
                .name("Furniture")
                .description("Cat furniture category")
                .build());

        Product product = Product.builder()
                .name("Cosmic star cat bed")
                .description("Soft and cozy bed for cats description must be long enough")
                .price(BigDecimal.valueOf(29.99))
                .currency("USD")
                .sku("COSMO-001")
                .category(savedCategory)
                .build();

        savedProduct = productRepository.save(product);
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/products - should return all products")
    void getAllProducts_ShouldReturnList() {
        mockMvc.perform(get("/api/v1/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cosmic star cat bed"))
                .andExpect(jsonPath("$[0].price").value(29.99));
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/products/{id} - should return product by ID")
    void getProductById_ShouldReturnProduct() {
        mockMvc.perform(get("/api/v1/products/" + savedProduct.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cosmic star cat bed"));
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/products/{id} - should return 404 when product not found")
    void getProductById_ShouldReturnNotFound() {
        mockMvc.perform(get("/api/v1/products/99999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    @DisplayName("POST /api/v1/products - should create new product")
    void createProduct_ShouldReturnCreatedProduct() {
        ProductDto newProductDto = new ProductDto(
                null,
                "Star Toy 3000",
                "Very fun laser toy for active cats",
                BigDecimal.valueOf(10.50),
                "USD",
                "LASER-01",
                savedCategory.getId()
        );

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
        ProductDto updateDto = new ProductDto(
                savedProduct.getId(),
                "Updated Bed Super star",
                "Updated soft and cozy bed description",
                BigDecimal.valueOf(50.00),
                "USD",
                "COSMO-001",
                savedCategory.getId()
        );

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
        mockMvc.perform(delete("/api/v1/products/" + savedProduct.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/products/" + savedProduct.getId()))
                .andExpect(status().isNotFound());
    }
}