package com.ssafy.runit.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.runit.domain.auth.dto.request.UserLoginRequest;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.auth.service.AuthService;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.ServerErrorCode;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "api/auth/";
    private static final String TEST_NUMBER = "1234";

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

    @Test
    @DisplayName("[회원가입] - 중복 사용자 검증")
    void registerUser_DuplicateUser_ThrowsException() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .userName("testUser")
                .userNumber(TEST_NUMBER)
                .userImageUrl("http://example.com/image.jpg")
                .build();
        doThrow(new CustomException(AuthErrorCode.DUPLICATED_USER_ERROR))
                .when(authService).registerUser(argThat(argument -> argument.getUserNumber().equals(TEST_NUMBER)));
        given()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when().post(BASE_URL + "register")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo(AuthErrorCode.DUPLICATED_USER_ERROR.message()))
                .body("errorCode", equalTo(AuthErrorCode.DUPLICATED_USER_ERROR.errorCode()));
    }

    @Test
    @DisplayName("[회원가입] - 데이터 유효성 검증")
    void registerUser_Invalid_Data_Form_ThrowsException() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .userName(null)
                .userNumber(TEST_NUMBER)
                .userImageUrl(null)
                .build();
        given()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when().post(BASE_URL + "register")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo(ServerErrorCode.METHOD_ARGUMENT_ERROR.message()))
                .body("errorCode", equalTo(ServerErrorCode.METHOD_ARGUMENT_ERROR.errorCode()));
    }

    @Test
    @DisplayName("[로그인] - 성공 검증")
    void loginUser_Success() throws Exception {
        UserLoginRequest request = new UserLoginRequest(TEST_NUMBER);
        given()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when().post(BASE_URL + "login")
                .then().log().all()
                .statusCode(200)
                .body("message", equalTo("로그인에 성공했습니다"))
                .body("data", nullValue());
    }
}