package com.cosmocats.cosmo_cats_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String orderNumber;
    private LocalDateTime createdAt;

    @NotBlank
    private String status;

    @Email
    @NotBlank
    private String customerEmail;

    @NotEmpty
    private List<Long> productIds;
}
