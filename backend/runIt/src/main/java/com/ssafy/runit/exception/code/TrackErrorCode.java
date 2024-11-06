package com.ssafy.runit.exception.code;

import com.ssafy.runit.exception.ErrorCodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TrackErrorCode implements ErrorCodeType {
    NOT_FOUND_TRACK_IMG(HttpStatus.NOT_FOUND, "Track-001", "이미지를 찾을 수 없습니다."),
    NOT_FOUND_TRACK_ROUTE(HttpStatus.NOT_FOUND, "Track-002", "경로를 찾을 수 없습니다.");

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
