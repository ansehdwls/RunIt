package com.ssafy.runit.exception.code;

import com.ssafy.runit.exception.ErrorCodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecordErrorCode implements ErrorCodeType {

    NOT_FOUND_RECORD_DATA(HttpStatus.NOT_FOUND, "Record-001", "없는 기록 데이터입니다."),
    NOT_FOUND_RECORD_DATA_LIST(HttpStatus.NOT_FOUND, "Record-002", "해당 유저의 기록 데이터가 없습니다."),
    NOT_ALLOW_DISTANCE(HttpStatus.BAD_REQUEST, "Record-003", "최소 저장 거리는 100m 입니다.");


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
