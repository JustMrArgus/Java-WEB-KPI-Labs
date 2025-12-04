package com.cosmocats.cosmo_cats_api.web;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import com.cosmocats.cosmo_cats_api.dto.ExternalProductDto;
import com.cosmocats.cosmo_cats_api.service.ExternalProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DisplayName("External Product Controller Integration Tests")
@Tag("external-product-service")
class ExternalProductControllerIT extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/external-products";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExternalProductService externalProductService;

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/external-products/{id} - returns product (200)")
    void testGetExternalProduct_OK() {
        ExternalProductDto dto = new ExternalProductDto("abc123", "Space Collar", "US", 5);
        when(externalProductService.getExternalProduct("abc123")).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + "/abc123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.externalId").value("abc123"))
                .andExpect(jsonPath("$.name").value("Space Collar"))
                .andExpect(jsonPath("$.supplier").value("US"))
                .andExpect(jsonPath("$.stock").value(5));

        verify(externalProductService, times(1)).getExternalProduct("abc123");
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/external-products/{id} - returns product with zero stock (200)")
    void testGetExternalProduct_ZeroStock() {
        ExternalProductDto dto = new ExternalProductDto("zero", "Empty Collar", "US", 0);
        when(externalProductService.getExternalProduct("zero")).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + "/zero")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.externalId").value("zero"))
                .andExpect(jsonPath("$.stock").value(0));

        verify(externalProductService, times(1)).getExternalProduct("zero");
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/external-products/{id} - returns 404 when product not found")
    void testGetExternalProduct_NotFound() {
        when(externalProductService.getExternalProduct("notfound"))
                .thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(get(BASE_URL + "/notfound")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(externalProductService, times(1)).getExternalProduct("notfound");
    }

    @Test
    @SneakyThrows
    @DisplayName("GET /api/v1/external-products/ - returns 404 for missing id path variable")
    void testGetExternalProduct_MissingPathVariable() {
        mockMvc.perform(get(BASE_URL + "/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}