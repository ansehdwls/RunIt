package com.ssafy.runit.config.security;

public final class SecurityConstants {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/",
            "/api/api-docs/**",
            "/api/swagger-ui/**",
            "/api/auth/**",
            "/error-docs"
    };

    private SecurityConstants() {
    }
}