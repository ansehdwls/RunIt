package com.ssafy.runit.domain.auth.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
