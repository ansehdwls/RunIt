package com.ssafy.runit.domain.auth.controller;

import com.ssafy.runit.ApiResponse;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;

public interface AuthDocs {
    ApiResponse<Void> register(UserRegisterRequest userRegisterRequest);
}
