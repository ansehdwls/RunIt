package com.ssafy.runit;

public record ApiResponse<T>(T data, String message) {

    public static <T> ApiResponse<T> create(T data, String message) {
        if (data == null) {
            return new ApiResponse<>(null, message);
        }
        return new ApiResponse<>(data, message);
    }
}
