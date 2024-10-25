package com.ssafy.runit.domain.auth.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController implements AuthDocs {

    private final AuthService authService;

    @Override
    @PostMapping("/register")
    public RunItApiResponse<Void> register(@RequestBody UserRegisterRequest request) {
        authService.registerUser(request);
        return RunItApiResponse.create(null, "회원가입에 성공했습니다");
    }
}
