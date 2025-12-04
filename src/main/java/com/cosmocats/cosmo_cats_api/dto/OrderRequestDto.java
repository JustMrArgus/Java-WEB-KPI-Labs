package com.cosmocats.cosmo_cats_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @NotBlank(message = "Status cannot be empty")
    private String status;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Customer email cannot be empty")
    private String customerEmail;

    @NotEmpty(message = "Order must contain at least one product")
    private List<Long> productIds;
}
