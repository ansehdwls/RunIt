package com.ssafy.runit.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.auth.service.AuthService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "api/auth/";

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("[회원가입]-성공 검증")
    void register_Success() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .userName("testUser")
                .userNumber("1234")
                .userImageUrl("image")
                .build();

        doNothing().when(authService).registerUser(any(UserRegisterRequest.class));
        given()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when().post(BASE_URL + "register")
                .then()
                .log().all()
                .statusCode(200)
                .body("message", equalTo("회원가입에 성공했습니다"))
                .body("data", nullValue());
    }
}