package com.cosmocats.cosmo_cats_api.repository.projection;

public interface ProductSalesProjection {
    Long getProductId();
    String getProductName();
    Long getOrderCount();
}
