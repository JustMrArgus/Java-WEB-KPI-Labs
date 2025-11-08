package com.cosmocats.cosmo_cats_api.service;

import com.cosmocats.cosmo_cats_api.aspect.FeatureToggle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CosmoCatService {

    @FeatureToggle(featureName = "cosmoCats")
    public List<String> getCosmoCats() {
        return List.of("CosmoCat 1", "CosmoCat 2", "Stardust Whiskers");
    }

    @FeatureToggle(featureName = "kittyProducts")
    public List<String> getKittyProducts() {
        return List.of("Laser Pointer", "Catnip Ball");
    }
}