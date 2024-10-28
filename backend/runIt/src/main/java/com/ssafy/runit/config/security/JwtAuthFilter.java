package com.ssafy.runit.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.ErrorCodeType;
import com.ssafy.runit.exception.ErrorResponse;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.util.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final ObjectMapper mapper = new ObjectMapper();

    private final JwtTokenProvider jwtTokenProvider;

    private static final List<String> EXCLUDE_URLS = Arrays.asList("/api/auth/", "/api/swagger-ui", "/error-docs");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
                String token = authHeader.substring(TOKEN_PREFIX.length()).trim();
                if (jwtTokenProvider.validateToken(token)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (CustomException e) {
            SecurityContextHolder.clearContext();
            sendErrorResponse(response, e.getErrorCodeType());
            return;
        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            sendErrorResponse(response, AuthErrorCode.EXPIRED_TOKEN_ERROR);
            return;
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            sendErrorResponse(response, AuthErrorCode.INVALID_TOKEN_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCodeType code) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponse errorResponse = ErrorResponse.error(code);
        String responseBody = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(responseBody);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDE_URLS.stream().anyMatch(path::startsWith);
    }
}
