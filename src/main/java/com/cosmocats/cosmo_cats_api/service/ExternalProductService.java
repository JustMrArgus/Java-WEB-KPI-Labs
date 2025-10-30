package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.dto.ExternalProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ExternalProductService {

    private final RestClient externalProductRestClient;

    public ExternalProductDto getExternalProduct(String id) {
        return externalProductRestClient.get()
                .uri("/api/v1/external-products/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ExternalProductDto.class);
    }
}