package com.ssafy.runit.config.security;

public final class SecurityConstants {
    public static final String[] CORS_ALLOWED_ORIGINS = {"http://3.35.169.202:8081"};
    public static final String[] CORS_ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"};
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