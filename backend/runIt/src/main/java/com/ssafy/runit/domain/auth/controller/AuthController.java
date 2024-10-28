package com.ssafy.runit.domain.auth.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.auth.dto.request.UpdateJwtRequest;
import com.ssafy.runit.domain.auth.dto.request.UserLoginRequest;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.auth.dto.response.LoginResponse;
import com.ssafy.runit.domain.auth.service.AuthService;
import com.ssafy.runit.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthDocs {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Override
    @PostMapping("/register")
    public RunItApiResponse<Void> register(@Valid @RequestBody UserRegisterRequest request) {
        authService.registerUser(request);
        return RunItApiResponse.create(null, "회원가입에 성공했습니다");
    }

    @Override
    @PostMapping("/login")
    public RunItApiResponse<LoginResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        LoginResponse loginResponse = authService.login(userLoginRequest);
        return RunItApiResponse.create(loginResponse, "로그인에 성공했습니다");
    }

    @Override
    @PostMapping("/token")
    public RunItApiResponse<LoginResponse> updateJWTToken(@Valid @RequestBody UpdateJwtRequest request) {
        LoginResponse response = authService.getNewRefreshToken(request);
        return RunItApiResponse.create(response, "JWT Token 갱신에 성공했습니다");
    }
}
