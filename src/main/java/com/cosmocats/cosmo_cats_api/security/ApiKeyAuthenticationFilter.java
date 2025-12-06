package com.cosmocats.cosmo_cats_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";
    
    private final String validApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String apiKey = request.getHeader(API_KEY_HEADER);
        
        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("API Key is missing in request to {}", request.getRequestURI());
            sendUnauthorizedResponse(response, "API Key is missing");
            return;
        }
        
        if (!validApiKey.equals(apiKey)) {
            log.warn("Invalid API Key provided for request to {}", request.getRequestURI());
            sendUnauthorizedResponse(response, "Invalid API Key");
            return;
        }
        
        log.debug("Valid API Key provided for request to {}", request.getRequestURI());
        
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "api-key-user",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_API_USER"))
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        filterChain.doFilter(request, response);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(String.format(
                "{\"error\": \"Unauthorized\", \"message\": \"%s\", \"status\": 401}", message));
    }
}
