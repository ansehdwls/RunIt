package com.ssafy.runit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCodeType {

    AUTHENTICATION_FAIL_ERROR(HttpStatus.UNAUTHORIZED, "AUTH-001", "사용자 인증에 실패했습니다."),
    EXPIRED_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "AUTH-002", "만료된 토큰입니다."),
    INVALID_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "AUTH-003", "유효하지 않은 토큰입니다.");

    private HttpStatus status;
    private String message;
    private String errorCode;

    AuthErrorCode(HttpStatus status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

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
