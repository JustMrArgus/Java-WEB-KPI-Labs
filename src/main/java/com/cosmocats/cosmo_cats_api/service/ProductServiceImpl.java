package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final Map<Long, Product> productStore = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public Product createProduct(Product product) {
        product.setId(idCounter++);
        productStore.put(product.getId(), product);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productStore.values());
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return Optional.ofNullable(productStore.get(id));
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        if (!productStore.containsKey(id)) {
            throw new ProductNotFoundException(id);
        }
        updatedProduct.setId(id);
        productStore.put(id, updatedProduct);
        return updatedProduct;
    }

    @Override
    public void deleteProductById(Long id) {
        productStore.remove(id);
    }
}