package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.domain.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    private final Map<Long, Product> productStore = new HashMap<>();
    private Long idCounter = 1L;

    public Product createProduct(Product product) {
        product.setId(idCounter++);
        productStore.put(product.getId(), product);
        return product;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productStore.values());
    }

    public Optional<Product> getProductById(Long id) {
        return Optional.ofNullable(productStore.get(id));
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        if (!productStore.containsKey(id)) {
            throw new IllegalArgumentException("Product with id " + id + " not found");
        }
        updatedProduct.setId(id);
        productStore.put(id, updatedProduct);
        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        if (!productStore.containsKey(id)) {
            throw new IllegalArgumentException("Product with id " + id + " not found");
        }
        productStore.remove(id);
    }
}
