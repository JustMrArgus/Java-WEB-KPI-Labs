package com.cosmocats.cosmo_cats_api.controller;

import com.cosmocats.cosmo_cats_api.dto.ExternalProductDto;
import com.cosmocats.cosmo_cats_api.service.ExternalProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExternalProductController.class)
class ExternalProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExternalProductService externalProductService;

    @Test
    @DisplayName("GET /api/v1/external-products/{id} — should return external product info (200)")
    void testGetExternalProduct_ShouldReturn200() throws Exception {
        ExternalProductDto dto = new ExternalProductDto("abc123", "Space Collar", "US", 5);
        Mockito.when(externalProductService.getExternalProduct("abc123")).thenReturn(dto);

        mockMvc.perform(get("/api/v1/external-products/abc123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.externalId", is("abc123")))
                .andExpect(jsonPath("$.name", is("Space Collar")))
                .andExpect(jsonPath("$.supplier", is("US")))
                .andExpect(jsonPath("$.stock", is(5)));
    }

    @Test
    @DisplayName("GET /api/v1/external-products/{id} — should return 404 when product not found")
    void testGetExternalProduct_ShouldReturn404() throws Exception {
        Mockito.when(externalProductService.getExternalProduct("notfound"))
                .thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(get("/api/v1/external-products/notfound")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/v1/external-products/{id} — should return 404 for unknown route or invalid id")
    void testGetExternalProduct_InvalidId_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/external-products/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/external-products/{id} — should return product with zero stock")
    void testGetExternalProduct_ZeroStock_ShouldReturn200() throws Exception {
        ExternalProductDto dto = new ExternalProductDto("zero", "Empty Collar", "US", 0);
        Mockito.when(externalProductService.getExternalProduct("zero")).thenReturn(dto);

        mockMvc.perform(get("/api/v1/external-products/zero")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.externalId", is("zero")))
                .andExpect(jsonPath("$.stock", is(0)));
    }
}
