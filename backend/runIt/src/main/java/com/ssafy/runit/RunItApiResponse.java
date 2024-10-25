package com.ssafy.runit;

public record RunItApiResponse<T>(T data, String message) {

    public static <T> RunItApiResponse<T> create(T data, String message) {
        if (data == null) {
            return new RunItApiResponse<>(null, message);
        }
        return new RunItApiResponse<>(data, message);
    }
}
