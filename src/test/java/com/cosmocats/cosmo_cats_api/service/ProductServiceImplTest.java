package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import com.cosmocats.cosmo_cats_api.domain.Category;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.exception.ProductNotFoundException;
import com.cosmocats.cosmo_cats_api.repository.CategoryRepository;
import com.cosmocats.cosmo_cats_api.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DisplayName("Product Service Tests")
public class ProductServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a product and return it with a new ID")
    void testCreateProduct_ShouldReturnProductWithId() {
        Product productToCreate = createDummyProduct("Cosmic Catnip");
        Product createdProduct = productService.createProduct(productToCreate);
        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getId());
        assertEquals("Cosmic Catnip", createdProduct.getName());
    }

    @Test
    @DisplayName("Should return all products when products exist")
    void testGetAllProducts_ShouldReturnAllProducts_WhenProductsExist() {
        Product product1 = productService.createProduct(createDummyProduct("Product 1"));
        Product product2 = productService.createProduct(createDummyProduct("Product 2"));
        List<Product> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    @DisplayName("Should return a product when ID exists")
    void testGetProductById_ShouldReturnProduct_WhenIdExists() {
        Product createdProduct = productService.createProduct(createDummyProduct("Galaxy Ball"));
        Long id = createdProduct.getId();
        Optional<Product> foundProduct = productService.getProductById(id);
        assertTrue(foundProduct.isPresent());
        assertEquals(id, foundProduct.get().getId());
        assertEquals("Galaxy Ball", foundProduct.get().getName());
    }

    @Test
    @DisplayName("Should return an empty Optional when ID does not exist")
    void testGetProductById_ShouldReturnEmptyOptional_WhenIdDoesNotExist() {
        Optional<Product> foundProduct = productService.getProductById(99L);
        assertTrue(foundProduct.isEmpty());
    }

    @Test
    @DisplayName("Should update and return the product when ID exists")
    void testUpdateProduct_ShouldUpdateAndReturnProduct_WhenIdExists() {
        Product originalProduct = productService.createProduct(createDummyProduct("Old Name"));
        Long id = originalProduct.getId();

        Category cat = categoryRepository.findAll().get(0);

        Product updatedProductDetails = Product.builder()
                .name("New Name")
                .description("New Desc")
                .price(BigDecimal.valueOf(200.0))
                .currency("EUR")
                .sku("SKU-NEW")
                .category(cat)
                .build();

        Product result = productService.updateProduct(id, updatedProductDetails);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("New Name", result.getName());
        assertEquals(BigDecimal.valueOf(200.0), result.getPrice());

        Optional<Product> productInStore = productService.getProductById(id);
        assertTrue(productInStore.isPresent());
        assertEquals("New Name", productInStore.get().getName());
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when updating a non-existent ID")
    void testUpdateProduct_ShouldThrowProductNotFoundException_WhenIdDoesNotExist() {
        Long nonExistentId = 99L;
        Category cat = categoryRepository.save(Category.builder().name("Temp").build());

        Product updatedProductDetails = Product.builder()
                .name("New Name")
                .description("Desc")
                .price(BigDecimal.TEN)
                .currency("USD")
                .sku("SKU-FAIL")
                .category(cat)
                .build();

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(nonExistentId, updatedProductDetails)
        );
        assertTrue(exception.getMessage().contains("99"));
    }

    @Test
    @DisplayName("Should delete the product when ID exists")
    void testDeleteProductById_ShouldRemoveProduct_WhenIdExists() {
        Product productToDelete = productService.createProduct(createDummyProduct("To Be Deleted"));
        Long id = productToDelete.getId();
        assertTrue(productService.getProductById(id).isPresent());
        productService.deleteProductById(id);
        assertTrue(productService.getProductById(id).isEmpty());
    }

    @Test
    @DisplayName("Should not throw an exception when deleting a non-existent ID")
    void testDeleteProductById_ShouldDoNothing_WhenIdDoesNotExist() {
        Long nonExistentId = 99L;

        assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProductById(nonExistentId));
    }

    private Product createDummyProduct(String name) {
        Category category = Category.builder()
                .name("Cat-" + name)
                .description("Description")
                .build();
        category = categoryRepository.save(category);

        return Product.builder()
                .name(name)
                .description("Test Description")
                .price(BigDecimal.valueOf(100.0))
                .currency("USD")
                .sku("SKU-" + name.hashCode())
                .category(category)
                .build();
    }
}