package com.ssafy.runit.domain.auth.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
}
