package com.cosmocats.cosmo_cats_api.aspect;

import com.cosmocats.cosmo_cats_api.config.FeatureToggleProperties;
import com.cosmocats.cosmo_cats_api.exception.FeatureNotAvailableException;
import com.cosmocats.cosmo_cats_api.service.CosmoCatService;
import com.cosmocats.cosmo_cats_api.service.FeatureToggleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = {
                FeatureToggleAspect.class,
                FeatureToggleService.class,
                FeatureToggleProperties.class,
                CosmoCatService.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@EnableAspectJAutoProxy
class FeatureToggleAspectTest {

    @Autowired
    private CosmoCatService cosmoCatService;

    @Autowired
    private FeatureToggleProperties featureToggleProperties;

    @BeforeEach
    void setup() {
        featureToggleProperties.getToggles().clear();
    }

    @Test
    @DisplayName("getCosmoCats should succeed when cosmoCats feature is enabled")
    void getCosmoCats_whenFeatureEnabled_shouldReturnList() {
        FeatureToggleProperties.Toggle t = new FeatureToggleProperties.Toggle();
        t.setEnabled(true);
        featureToggleProperties.getToggles().put("cosmoCats", t);

        Class<?> actualClass = AopProxyUtils.ultimateTargetClass(cosmoCatService);
        assertNotNull(actualClass);

        List<String> cats = cosmoCatService.getCosmoCats();

        assertNotNull(cats);
        assertFalse(cats.isEmpty());
        assertTrue(cats.stream().anyMatch(s -> s.toLowerCase().contains("cosmo") || s.toLowerCase().contains("stardust")));
    }

    @Test
    @DisplayName("getCosmoCats should throw FeatureNotAvailableException when cosmoCats feature is disabled")
    void getCosmoCats_whenFeatureDisabled_shouldThrow() {
        FeatureToggleProperties.Toggle t = new FeatureToggleProperties.Toggle();
        t.setEnabled(false);
        featureToggleProperties.getToggles().put("cosmoCats", t);

        FeatureNotAvailableException ex = assertThrows(
                FeatureNotAvailableException.class,
                () -> cosmoCatService.getCosmoCats()
        );

        assertTrue(ex.getMessage().contains("cosmoCats"));
    }

    @Test
    @DisplayName("getKittyProducts should succeed when kittyProducts feature is enabled")
    void getKittyProducts_whenFeatureEnabled_shouldReturnList() {
        FeatureToggleProperties.Toggle t = new FeatureToggleProperties.Toggle();
        t.setEnabled(true);
        featureToggleProperties.getToggles().put("kittyProducts", t);

        List<String> products = cosmoCatService.getKittyProducts();

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertTrue(products.stream().anyMatch(p -> p.toLowerCase().contains("laser") || p.toLowerCase().contains("catnip")));
    }

    @Test
    @DisplayName("getKittyProducts should throw FeatureNotAvailableException when kittyProducts feature is disabled")
    void getKittyProducts_whenFeatureDisabled_shouldThrow() {
        FeatureToggleProperties.Toggle t = new FeatureToggleProperties.Toggle();
        t.setEnabled(false);
        featureToggleProperties.getToggles().put("kittyProducts", t);

        FeatureNotAvailableException ex = assertThrows(
                FeatureNotAvailableException.class,
                () -> cosmoCatService.getKittyProducts()
        );

        assertTrue(ex.getMessage().toLowerCase().contains("kittyproducts"));
    }
}
