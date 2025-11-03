package com.cosmocats.cosmo_cats_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.dto.ProductDto;
import com.cosmocats.cosmo_cats_api.exception.ProductNotFoundException;
import com.cosmocats.cosmo_cats_api.mapper.ProductMapper;
import com.cosmocats.cosmo_cats_api.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("Product Controller Integration Tests")
@Tag("product-controller")
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        reset(productService);

        product = new Product(
                1L,
                "Cosmic star cat bed",
                "Soft and cozy bed for cats",
                29.99,
                "USD",
                "COSMO-001",
                100L
        );

        productDto = new ProductDto(
                1L,
                "Cosmic star cat bed",
                "Soft and cozy bed for cats",
                29.99,
                "USD",
                "COSMO-001",
                100L
        );
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/products - should return all products")
    void getAllProducts_ShouldReturnList() {
        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cosmic star cat bed"))
                .andExpect(jsonPath("$[0].price").value(29.99));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/products/{id} - should return product by ID")
    void getProductById_ShouldReturnProduct() {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cosmic star cat bed"));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/products/{id} - should return 404 when product not found")
    void getProductById_ShouldReturnNotFound() {
        when(productService.getProductById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(999L);
    }

    @Test
    @SneakyThrows
    @DisplayName("POST /api/v1/products - should create new product")
    void createProduct_ShouldReturnCreatedProduct() {
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cosmic star cat bed"));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT /api/v1/products/{id} - should update existing product")
    void updateProduct_ShouldReturnUpdatedProduct() {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cosmic star cat bed"));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT /api/v1/products/{id} - should return 404 when product not found")
    void updateProduct_ShouldReturnNotFound() {
        when(productService.updateProduct(eq(999L), any(Product.class)))
                .thenThrow(new ProductNotFoundException(999L));

        mockMvc.perform(put("/api/v1/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).updateProduct(eq(999L), any(Product.class));
    }

    @Test
    @SneakyThrows
    @DisplayName("DELETE /api/v1/products/{id} - should delete product")
    void deleteProduct_ShouldReturnNoContent() {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProductById(1L);
    }

    @Test
    @SneakyThrows
    @DisplayName("DELETE /api/v1/products/{id} - should return 404 when product not found")
    void deleteProduct_ShouldReturnNotFound() {
        doThrow(new ProductNotFoundException(999L))
                .when(productService).deleteProductById(999L);

        mockMvc.perform(delete("/api/v1/products/999"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).deleteProductById(999L);
    }
}