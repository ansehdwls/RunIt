package com.ssafy.runit.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCodeType errorCodeType;

    public CustomException(ErrorCodeType errorCodeType) {
        this.errorCodeType = errorCodeType;
    }

    HttpStatus httpStatus() {
        return errorCodeType.httpStatus();
    }

    String message() {
        return errorCodeType.message();
    }

    String errorCode() {
        return errorCodeType.errorCode();
    }
}
