package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.entity.CategoryEntity;
import com.cosmocats.cosmo_cats_api.entity.ProductEntity;
import com.cosmocats.cosmo_cats_api.exception.CategoryNotFoundException;
import com.cosmocats.cosmo_cats_api.exception.ProductNotFoundException;
import com.cosmocats.cosmo_cats_api.mapper.CategoryEntityMapper;
import com.cosmocats.cosmo_cats_api.mapper.ProductEntityMapper;
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
public class ProductServiceImpl implements ProductService {

    private static final String DEFAULT_REPORT_STATUS = "COMPLETED";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductEntityMapper productEntityMapper;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        CategoryEntity categoryEntity = resolveCategory(product.getCategory());
        ProductEntity entity = productEntityMapper.toEntity(product);
        entity.setCategory(categoryEntity);
        ProductEntity saved = productRepository.save(entity);
        return productEntityMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productEntityMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        ProductEntity existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCurrency(updatedProduct.getCurrency());
        existing.setSku(updatedProduct.getSku());
        if (updatedProduct.getCategory() != null) {
            existing.setCategory(resolveCategory(updatedProduct.getCategory()));
        }
        ProductEntity saved = productRepository.save(existing);
        return productEntityMapper.toDomain(saved);
    }

    @Override
    @Transactional
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

    private CategoryEntity resolveCategory(Category category) {
        if (category == null || category.getId() == null) {
            throw new CategoryNotFoundException(null);
        }
        return categoryRepository.findById(category.getId())
                .orElseThrow(() -> new CategoryNotFoundException(category.getId()));
    }
}