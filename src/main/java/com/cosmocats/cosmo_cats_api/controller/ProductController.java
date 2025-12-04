package com.cosmocats.cosmo_cats_api.controller;

import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.dto.ProductRequestDto;
import com.cosmocats.cosmo_cats_api.dto.ProductResponseDto;
import com.cosmocats.cosmo_cats_api.dto.ProductSalesReportDto;
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
    public ProductResponseDto create(@Valid @RequestBody ProductRequestDto requestDto) {
        Product product = productMapper.toDomain(requestDto);
        return productMapper.toResponseDto(productService.createProduct(product));
    }

    @GetMapping("/api/v1/products")
    public List<ProductResponseDto> getAll() {
        return productService.getAllProducts()
            .stream()
            .map(productMapper::toResponseDto)
            .toList();
    }

    @GetMapping("/api/v1/products/{id}")
    public ProductResponseDto getById(@PathVariable Long id) {
        return productService.getProductById(id)
            .map(productMapper::toResponseDto)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PutMapping("/api/v1/products/{id}")
    public ProductResponseDto update(@PathVariable Long id, @Valid @RequestBody ProductRequestDto requestDto) {
        Product product = productMapper.toDomain(requestDto);
        return productMapper.toResponseDto(productService.updateProduct(id, product));
    }

    @DeleteMapping("/api/v1/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @GetMapping("/api/v1/products/top")
    public List<ProductSalesReportDto> getTopSellingProducts(@RequestParam(defaultValue = "5") int limit) {
        return productService.getTopSellingProducts(limit).stream()
                .map(projection -> new ProductSalesReportDto(
                        projection.getProductId(),
                        projection.getProductName(),
                        projection.getOrderCount()))
                .toList();
    }
}