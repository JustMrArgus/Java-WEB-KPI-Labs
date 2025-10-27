package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(Product product);

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product updateProduct(Long id, Product updatedProduct);

    void deleteProductById(Long id);
}