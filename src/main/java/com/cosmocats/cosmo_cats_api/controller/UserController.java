package com.cosmocats.cosmo_cats_api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> userInfo = new HashMap<>();
        
        if (principal != null) {
            userInfo.put("name", principal.getAttribute("name"));
            userInfo.put("login", principal.getAttribute("login"));
            userInfo.put("avatar_url", principal.getAttribute("avatar_url"));
            userInfo.put("email", principal.getAttribute("email"));
            userInfo.put("authenticated", true);
        } else {
            userInfo.put("authenticated", false);
            userInfo.put("message", "User is not authenticated");
        }
        
        return userInfo;
    }
}
