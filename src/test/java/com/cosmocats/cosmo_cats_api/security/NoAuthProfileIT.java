package com.cosmocats.cosmo_cats_api.security;

import com.cosmocats.cosmo_cats_api.intergation.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("no-auth")
@DisplayName("No-Auth Profile Integration Test")
class NoAuthProfileIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should access protected endpoint without authentication when 'no-auth' profile is active")
    void shouldAccessWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk());
    }
}