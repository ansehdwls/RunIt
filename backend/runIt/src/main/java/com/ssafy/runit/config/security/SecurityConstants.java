package com.ssafy.runit.config.security;

public final class SecurityConstants {
    public static final String[] CORS_ALLOWED_ORIGINS = {
            "http://localhost:8081", "http://3.35.169.202:8081", "https://k11d102.p.ssafy.io"
    };
    public static final String[] CORS_ALLOWED_METHODS = {"*"};
    public static final String[] CORS_ALLOWED_HEADERS = {"*"};
    public static final String[] CORS_EXPOSE_HEADER = {"Authorization"};
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