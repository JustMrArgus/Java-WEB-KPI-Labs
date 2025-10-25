package com.cosmocats.cosmo_cats_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${external.api.baseurl}")
    private String externalApiBaseUrl;

    @Bean
    public RestClient externalProductRestClient() {
        return RestClient.builder()
                .baseUrl(externalApiBaseUrl)
                .build();
    }
}