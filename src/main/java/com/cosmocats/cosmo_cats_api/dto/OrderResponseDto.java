package com.cosmocats.cosmo_cats_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;

    private String orderNumber;

    private LocalDateTime createdAt;

    private String status;

    private String customerEmail;

    private List<Long> productIds;
}
