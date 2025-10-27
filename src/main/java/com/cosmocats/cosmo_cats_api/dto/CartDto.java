package com.cosmocats.cosmo_cats_api.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private Long id;
    private String sessionId;
    private List<Long> productIds;
}
