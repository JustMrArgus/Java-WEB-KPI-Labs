package com.cosmocats.cosmo_cats_api.controller;

import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.dto.ProductDto;
import com.cosmocats.cosmo_cats_api.exception.GlobalExceptionHandler;
import com.cosmocats.cosmo_cats_api.exception.ProductNotFoundException;
import com.cosmocats.cosmo_cats_api.mapper.ProductMapper;
import com.cosmocats.cosmo_cats_api.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@Import(GlobalExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    private final Product product = new Product(
            1L,
            "Cosmic star cat bed",
            "Soft and cozy bed for cats",
            29.99,
            "USD",
            "COSMO-001",
            100L
    );
    private final ProductDto productDto = new ProductDto(
            1L,
            "Cosmic star cat bed",
            "Soft and cozy bed for cats",
            29.99,
            "USD",
            "COSMO-001",
            100L
    );

    @Test
    @DisplayName("GET /api/v1/products - should return all products")
    void getAllProducts_ShouldReturnList() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(product));
        when(productMapper.toProductDto(any(Product.class))).thenReturn(productDto);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Cosmic star cat bed")))
                .andExpect(jsonPath("$[0].price", is(29.99)));
    }

    @Test
    @DisplayName("GET /api/v1/products/{id} - should return product by ID")
    void getProductById_ShouldReturnProduct() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toProductDto(product)).thenReturn(productDto);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cosmic star cat bed")));
    }

    @Test
    @DisplayName("GET /api/v1/products/{id} - should return 404 when product not found")
    void getProductById_ShouldReturnNotFound() throws Exception {
        when(productService.getProductById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/products - should create new product")
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        when(productMapper.toProductEntity(any(ProductDto.class))).thenReturn(product);
        when(productService.createProduct(any(Product.class))).thenReturn(product);
        when(productMapper.toProductDto(any(Product.class))).thenReturn(productDto);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Cosmic star cat bed")));
    }

    @Test
    @DisplayName("PUT /api/v1/products/{id} - should update existing product")
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        when(productMapper.toProductEntity(any(ProductDto.class))).thenReturn(product);
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(product);
        when(productMapper.toProductDto(any(Product.class))).thenReturn(productDto);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cosmic star cat bed")));
    }

    @Test
    @DisplayName("PUT /api/v1/products/{id} - should return 404 when product not found")
    void updateProduct_ShouldReturnNotFound() throws Exception {
        when(productMapper.toProductEntity(any(ProductDto.class))).thenReturn(product);
        when(productService.updateProduct(eq(999L), any(Product.class)))
                .thenThrow(new ProductNotFoundException(999L));

        mockMvc.perform(put("/api/v1/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("DELETE /api/v1/products/{id} - should delete product")
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/products/{id} - should return 404 when product not found")
    void deleteProduct_ShouldReturnNotFound() throws Exception {
        doThrow(new ProductNotFoundException(999L))
                .when(productService).deleteProductById(999L);

        mockMvc.perform(delete("/api/v1/products/999"))
                .andExpect(status().isNotFound());
    }
}