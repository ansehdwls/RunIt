package com.ssafy.runit.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private String errorCode;
    private String message;

    public static  ErrorResponse error(ErrorCodeType errorCodeType) {
        return ErrorResponse.builder()
                .errorCode(errorCodeType.errorCode())
                .message(errorCodeType.message())
                .build();
    }
}
