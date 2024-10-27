package com.ssafy.runit.domain.auth.service;

import com.ssafy.runit.domain.auth.dto.request.UserLoginRequest;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.auth.dto.response.LoginResponse;

public interface AuthService {
    void registerUser(UserRegisterRequest request);

    LoginResponse login(UserLoginRequest request);

    void saveRefreshToken(String userEmail, String refreshToken);
}
