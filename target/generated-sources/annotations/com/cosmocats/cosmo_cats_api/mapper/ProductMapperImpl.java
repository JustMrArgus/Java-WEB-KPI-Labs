package com.cosmocats.cosmo_cats_api.mapper;

import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.dto.ProductDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-25T03:51:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Red Hat, Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toProductDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( product.getId() );
        productDto.setName( product.getName() );
        productDto.setDescription( product.getDescription() );
        productDto.setPrice( product.getPrice() );
        productDto.setCurrency( product.getCurrency() );
        productDto.setSku( product.getSku() );
        productDto.setCategoryId( product.getCategoryId() );

        return productDto;
    }

    @Override
    public Product toProductEntity(ProductDto dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( dto.getId() );
        product.setName( dto.getName() );
        product.setDescription( dto.getDescription() );
        product.setPrice( dto.getPrice() );
        product.setCurrency( dto.getCurrency() );
        product.setSku( dto.getSku() );
        product.setCategoryId( dto.getCategoryId() );

        return product;
    }
}
