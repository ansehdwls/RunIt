package com.ssafy.runit.exception.code;

import com.ssafy.runit.exception.ErrorCodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCodeType {

    AUTHENTICATION_FAIL_ERROR(HttpStatus.UNAUTHORIZED, "AUTH-001", "사용자 인증에 실패했습니다."),
    EXPIRED_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "AUTH-002", "만료된 토큰입니다."),
    INVALID_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "AUTH-003", "유효하지 않은 토큰입니다."),
    INVALID_DATA_FORM(HttpStatus.BAD_REQUEST, "AUTH-004", "데이터 형식을 확인해주세요."),
    DUPLICATED_USER_ERROR(HttpStatus.BAD_REQUEST, "AUTH-005", "이미 가입된 사용자입니다."),
    UNREGISTERED_USER_ERROR(HttpStatus.BAD_REQUEST, "AUTH-006", "회원가입을 진행해주세요");

    private final HttpStatus status;
    private final String errorCode;
    private final String message;


    @Override
    public HttpStatus httpStatus() {
        return this.status;
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public String errorCode() {
        return this.errorCode;
    }
}
