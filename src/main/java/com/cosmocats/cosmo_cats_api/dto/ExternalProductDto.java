package com.cosmocats.cosmo_cats_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalProductDto {
    private String externalId;
    private String name;
    private String supplier;
    private int stock;
}