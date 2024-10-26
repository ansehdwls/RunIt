package com.ssafy.runit.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.runit.exception.ErrorResponse;
import com.ssafy.runit.exception.code.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println(authException.getMessage());
        ErrorResponse error = ErrorResponse.error(AuthErrorCode.AUTHENTICATION_FAIL_ERROR);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(AuthErrorCode.AUTHENTICATION_FAIL_ERROR.getStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}
