package com.cosmocats.cosmo_cats_api.controller;

import com.cosmocats.cosmo_cats_api.dto.ExternalProductDto;
import com.cosmocats.cosmo_cats_api.service.ExternalProductService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/external-products")
@RequiredArgsConstructor
public class ExternalProductController {

    private final ExternalProductService externalProductService;

    @GetMapping("/{id}")
    public ExternalProductDto getExternalProductById(@PathVariable @NotBlank String id) {
        return externalProductService.getExternalProduct(id);
    }
}