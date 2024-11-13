package com.ssafy.runit.exception.code;

import com.ssafy.runit.exception.ErrorCodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SplitErrorCode implements ErrorCodeType {

    INVALID_SPLIT_LIST(HttpStatus.BAD_REQUEST, "SPLIT-001", "구간 정보는 반드시 기록되어야합니다."),
    PACE_CALCULATION_ERROR(HttpStatus.BAD_REQUEST, "SPLIT-002", "페이스를 계산할 수 없습니다."),
    INVALID_DISTANCE(HttpStatus.BAD_REQUEST, "SPLIT-004", "거리 측정에 오류가 있습니다.");

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
