package com.ssafy.runit.domain.auth.service;

import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;

public interface AuthService {
    void registerUser(UserRegisterRequest request);
}
