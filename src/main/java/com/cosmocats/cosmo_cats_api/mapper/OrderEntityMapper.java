package com.cosmocats.cosmo_cats_api.mapper;

import com.cosmocats.cosmo_cats_api.domain.Order;
import com.cosmocats.cosmo_cats_api.domain.Product;
import com.cosmocats.cosmo_cats_api.entity.OrderEntity;
import com.cosmocats.cosmo_cats_api.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ProductEntityMapper.class})
public interface OrderEntityMapper {

    @Mapping(target = "products", expression = "java(toProductDomainSet(entity.getProducts()))")
    Order toDomain(OrderEntity entity);

    @Mapping(target = "products", expression = "java(toProductEntitySet(domain.getProducts()))")
    OrderEntity toEntity(Order domain);

    default Set<Product> toProductDomainSet(Set<ProductEntity> entities) {
        if (entities == null) {
            return new HashSet<>();
        }
        return entities.stream()
                .map(entity -> Product.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .description(entity.getDescription())
                        .price(entity.getPrice())
                        .currency(entity.getCurrency())
                        .sku(entity.getSku())
                        .build())
                .collect(Collectors.toSet());
    }

    default Set<ProductEntity> toProductEntitySet(Set<Product> domains) {
        if (domains == null) {
            return new HashSet<>();
        }
        return domains.stream()
                .map(domain -> ProductEntity.builder()
                        .id(domain.getId())
                        .name(domain.getName())
                        .description(domain.getDescription())
                        .price(domain.getPrice())
                        .currency(domain.getCurrency())
                        .sku(domain.getSku())
                        .build())
                .collect(Collectors.toSet());
    }
}
