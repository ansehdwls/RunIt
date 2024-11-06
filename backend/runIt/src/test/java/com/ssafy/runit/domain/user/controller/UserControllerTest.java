package com.ssafy.runit.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.runit.config.security.CustomUserDetails;
import com.ssafy.runit.config.security.CustomUserDetailsService;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.user.dto.request.FcmTokenRequest;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.service.UserService;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.ServerErrorCode;
import com.ssafy.runit.util.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String BASE_URL = "api/user/";
    private static final String TEST_NUMBER = "1234";
    private static String refreshToken = "";
    private static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token";
    private static final String TOKEN_PREFIX = "Bearer ";
    private Group testGroup;
    private User testUser;
    private CustomUserDetails customUserDetails;


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        refreshToken = jwtTokenProvider.generateRefreshToken(TEST_NUMBER);
        testGroup = Group.builder().id(1L).build();
        testUser = User
                .builder()
                .id(1L)
                .userNumber(TEST_NUMBER)
                .userGroup(testGroup)
                .userName("test user")
                .imageUrl("http://test.com/image.png")
                .fcmToken("fcmToken")
                .build();
        customUserDetails = new CustomUserDetails(testUser);
    }

    @Test
    @DisplayName("[내 정보 조회] - 인증된 사용자 검증")
    public void geyMyInfo_Success() {
        when(customUserDetailsService.loadUserByUsername(TEST_NUMBER)).thenReturn(customUserDetails);
        when(userService.findByUserNumber(TEST_NUMBER)).thenReturn(testUser);
        given()
                .header("Authorization", TOKEN_PREFIX + refreshToken) // JWT 토큰 포함
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URL + "me")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.userNumber", equalTo(testUser.getUserNumber()))
                .body("data.userName", equalTo(testUser.getUserName()))
                .body("data.imageUrl", equalTo(testUser.getImageUrl()))
                .body("data.userId", equalTo(testUser.getId().intValue()))
                .body("data.groupId", equalTo(testGroup.getId().intValue()))
                .body("message", equalTo("사용자 정보 조회에 성공했습니다."));
        verify(userService, times(1)).findByUserNumber(eq(TEST_NUMBER));
    }

    @Test
    @DisplayName("[내 정보 조회] - 인증되지 않은 사용자 접근 검증")
    public void geyMyInfo_Unauthenticated_ThrowsException() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(BASE_URL + "me")
                .then()
                .log().all()
                .statusCode(AuthErrorCode.AUTHENTICATION_FAIL_ERROR.getStatus().value()) // Unauthorized
                .body("errorCode", equalTo(AuthErrorCode.AUTHENTICATION_FAIL_ERROR.getErrorCode()))
                .body("message", equalTo(AuthErrorCode.AUTHENTICATION_FAIL_ERROR.getMessage()));
        verify(userService, never()).findByUserNumber(eq(TEST_NUMBER));
    }


    @Test
    @DisplayName("[내 정보 조회] - 유효하지 않은 토큰 검증")
    public void geyMyInfo_Invalid_Token_ThrowsException() {
        given()
                .header("Authorization", TOKEN_PREFIX + INVALID_REFRESH_TOKEN) // JWT 토큰 포함
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(BASE_URL + "me")
                .then()
                .log().all()
                .statusCode(AuthErrorCode.INVALID_TOKEN_ERROR.getStatus().value()) // Unauthorized
                .body("errorCode", equalTo(AuthErrorCode.INVALID_TOKEN_ERROR.getErrorCode()))
                .body("message", equalTo(AuthErrorCode.INVALID_TOKEN_ERROR.getMessage()));
        verify(userService, never()).findByUserNumber(eq(TEST_NUMBER));
    }

    @Test
    @DisplayName("[Fcm Token] 성공 검증")
    public void saveFcmToken_Success() throws Exception {
        FcmTokenRequest request = new FcmTokenRequest("newFcmToken");
        when(customUserDetailsService.loadUserByUsername(TEST_NUMBER)).thenReturn(customUserDetails);
        given()
                .header("Authorization", TOKEN_PREFIX + refreshToken) // JWT 토큰 포함
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .patch(BASE_URL + "fcmToken")
                .then()
                .log().all()
                .statusCode(200)
                .body("data", nullValue())
                .body("message", equalTo("FcmToken이 저장되었습니다."));
        verify(customUserDetailsService).loadUserByUsername(eq(TEST_NUMBER));
        verify(userService).saveFcmToken(eq(TEST_NUMBER), eq(request.getFcmToken()));
    }

    @Test
    @DisplayName("[Fcm Token] 데이터 유효성 검증")
    public void saveFcmToken_Invalid_Data_Form_ThrowsException() throws Exception {
        FcmTokenRequest request = new FcmTokenRequest(null);
        when(customUserDetailsService.loadUserByUsername(TEST_NUMBER)).thenReturn(customUserDetails);
        given()
                .header("Authorization", TOKEN_PREFIX + refreshToken) // JWT 토큰 포함
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .patch(BASE_URL + "fcmToken")
                .then()
                .log().all()
                .statusCode(ServerErrorCode.METHOD_ARGUMENT_ERROR.getStatus().value())
                .body("errorCode", equalTo(ServerErrorCode.METHOD_ARGUMENT_ERROR.errorCode()))
                .body("message", equalTo(ServerErrorCode.METHOD_ARGUMENT_ERROR.message()));
        verify(customUserDetailsService).loadUserByUsername(eq(TEST_NUMBER));
        verify(userService, never()).saveFcmToken(eq(TEST_NUMBER), eq(request.getFcmToken()));
    }
}
