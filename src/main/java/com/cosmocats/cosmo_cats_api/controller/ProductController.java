package com.cosmocats.cosmo_cats_api.controller;

import com.cosmocats.cosmo_cats_api.dto.ProductDto;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.mapper.ProductMapper;
import com.cosmocats.cosmo_cats_api.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping("/api/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@Valid @RequestBody ProductDto dto) {
        Product product = productMapper.toProductEntity(dto);
        return productMapper.toProductDto(productService.createProduct(product));
    }

    @GetMapping("/api/products")
    public List<ProductDto> getAll() {
        return productService.getAllProducts()
            .stream()
            .map(productMapper::toProductDto)
            .toList();
    }

    @GetMapping("/api/products/{id}")
    public ProductDto getById(@PathVariable Long id) {
        return productService.getProductById(id)
            .map(productMapper::toProductDto)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @PutMapping("/api/products/{id}")
    public ProductDto update(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        Product product = productMapper.toProductEntity(dto);
        return productMapper.toProductDto(productService.updateProduct(id, product));
    }

    @DeleteMapping("/api/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
