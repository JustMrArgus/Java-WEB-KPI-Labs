package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.config.FeatureToggleProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureToggleService {

    private final FeatureToggleProperties featureToggleProperties;

    public boolean isFeatureEnabled(String featureName) {
        FeatureToggleProperties.Toggle toggle = featureToggleProperties.getToggles().get(featureName);

        if (toggle != null) {
            return toggle.isEnabled();
        }

        return false;
    }
}