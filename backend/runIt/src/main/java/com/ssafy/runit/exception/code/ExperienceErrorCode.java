package com.ssafy.runit.exception.code;

import com.ssafy.runit.exception.ErrorCodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExperienceErrorCode implements ErrorCodeType {

    SAVE_EXPERIENCE_FAIL(HttpStatus.UNAUTHORIZED, "Experience-001", "경험치 저장에 실패했습니다.");

    private final HttpStatus status;
    private final String errorCode;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return null;
    }

    @Override
    public String message() {
        return "";
    }

    @Override
    public String errorCode() {
        return "";
    }
}
