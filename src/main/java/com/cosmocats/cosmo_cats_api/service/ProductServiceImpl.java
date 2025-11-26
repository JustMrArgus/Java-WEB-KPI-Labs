package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.exception.CategoryNotFoundException;
import com.cosmocats.cosmo_cats_api.exception.ProductNotFoundException;
import com.cosmocats.cosmo_cats_api.repository.CategoryRepository;
import com.cosmocats.cosmo_cats_api.repository.ProductRepository;
import com.cosmocats.cosmo_cats_api.repository.projection.ProductSalesProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final String DEFAULT_REPORT_STATUS = "COMPLETED";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(Product product) {
        product.setCategory(resolveCategory(product.getCategory()));
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCurrency(updatedProduct.getCurrency());
        existing.setSku(updatedProduct.getSku());
        if (updatedProduct.getCategory() != null) {
            existing.setCategory(resolveCategory(updatedProduct.getCategory()));
        }
        return productRepository.save(existing);
    }

    @Override
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSalesProjection> getTopSellingProducts(int limit) {
        int pageSize = Math.max(1, limit);
        return productRepository.findTopProductsByStatus(DEFAULT_REPORT_STATUS, PageRequest.of(0, pageSize));
    }

    private Category resolveCategory(Category category) {
        if (category == null || category.getId() == null) {
            throw new CategoryNotFoundException(null);
        }
        return categoryRepository.findById(category.getId())
                .orElseThrow(() -> new CategoryNotFoundException(category.getId()));
    }
}