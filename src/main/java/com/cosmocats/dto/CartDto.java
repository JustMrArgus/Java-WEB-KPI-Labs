package com.cosmocats.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private Long id;
    private String sessionId;
    private List<Long> productIds;
}
