package com.cosmocats.cosmo_cats_api.dto;

import lombok.Value;

@Value
public class ProductSalesReportDto {
    Long productId;
    String productName;
    Long orderCount;
}
