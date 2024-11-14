package com.ssafy.runit.domain.attendance.controller;

import com.ssafy.runit.config.security.CustomUserDetails;
import com.ssafy.runit.config.security.CustomUserDetailsService;
import com.ssafy.runit.domain.attendance.dto.response.DayAttendanceResponse;
import com.ssafy.runit.domain.attendance.service.AttendanceService;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.util.DateUtils;
import com.ssafy.runit.util.JwtTokenProvider;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AttendanceControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private AttendanceService attendanceService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String BASE_URL = "api/attendance/";
    private static final String TEST_NUMBER = "1234";
    private static String refreshToken = "";
    private static final String TOKEN_PREFIX = "Bearer ";
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
        refreshToken = jwtTokenProvider.generateRefreshToken(TEST_NUMBER);
        User testUser = User
                .builder()
                .id(1L)
                .userNumber(TEST_NUMBER)
                .build();
        customUserDetails = new CustomUserDetails(testUser);
    }

    @Test
    @DisplayName("[주간 출석 조회] 기록이 있을 경우 검증")
    void getWeeklyAttendance_AllAttended_Success() {
        when(customUserDetailsService.loadUserByUsername(eq(TEST_NUMBER))).thenReturn(customUserDetails);
        List<DayAttendanceResponse> response = Stream.of(DayOfWeek.values()).map(
                day -> {
                    LocalDate targetDate = DateUtils.getLastMonday().plusDays(day.getValue() - 1);
                    return DayAttendanceResponse.fromEntity(DateUtils.getDayNameInKorean(day), targetDate, true);
                }).toList();
        when(attendanceService.getWeekAttendance(eq(TEST_NUMBER))).thenReturn(response);
        given()
                .header("Authorization", TOKEN_PREFIX + refreshToken) // JWT 토큰 포함
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(BASE_URL + "week")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.size()", equalTo(7))
                .body("message", equalTo("주간 출석 조회에 성공했습니다."));
        verify(customUserDetailsService, times(1)).loadUserByUsername(eq(TEST_NUMBER));
        verify(attendanceService, times(1)).getWeekAttendance(eq(TEST_NUMBER));
    }


    @Test
    @DisplayName("[주간 출석 조회] 기록이 없을 경우 검증")
    void getWeeklyAttendance_NoAttendance_Success() {
        when(customUserDetailsService.loadUserByUsername(eq(TEST_NUMBER))).thenReturn(customUserDetails);
        given()
                .header("Authorization", TOKEN_PREFIX + refreshToken) // JWT 토큰 포함
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(BASE_URL + "week")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.size()", equalTo(0))
                .body("message", equalTo("주간 출석 조회에 성공했습니다."));
        verify(customUserDetailsService, times(1)).loadUserByUsername(eq(TEST_NUMBER));
        verify(attendanceService, times(1)).getWeekAttendance(eq(TEST_NUMBER));
    }
}
