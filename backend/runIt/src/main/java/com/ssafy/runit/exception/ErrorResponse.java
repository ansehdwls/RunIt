package com.ssafy.runit.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse<T> {
    private String errorCode;
    private String message;

    public static <T> ErrorResponse<T> error(ErrorCodeType errorCodeType) {
        return ErrorResponse.<T>builder()
                .errorCode(errorCodeType.errorCode())
                .message(errorCodeType.message())
                .build();
    }
}
