package com.cosmocats.cosmo_cats_api.controller;

import com.cosmocats.cosmo_cats_api.dto.ProductDto;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.exception.ProductNotFoundException;
import com.cosmocats.cosmo_cats_api.mapper.ProductMapper;
import com.cosmocats.cosmo_cats_api.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping("/api/v1/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@Valid @RequestBody ProductDto dto) {
        Product product = productMapper.toProductEntity(dto);
        return productMapper.toProductDto(productService.createProduct(product));
    }

    @GetMapping("/api/v1/products")
    public List<ProductDto> getAll() {
        return productService.getAllProducts()
            .stream()
            .map(productMapper::toProductDto)
            .toList();
    }

    @GetMapping("/api/v1/products/{id}")
    public ProductDto getById(@PathVariable Long id) {
        return productService.getProductById(id)
            .map(productMapper::toProductDto)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PutMapping("/api/v1/products/{id}")
    public ProductDto update(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        Product product = productMapper.toProductEntity(dto);
        return productMapper.toProductDto(productService.updateProduct(id, product));
    }

    @DeleteMapping("/api/v1/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }
}