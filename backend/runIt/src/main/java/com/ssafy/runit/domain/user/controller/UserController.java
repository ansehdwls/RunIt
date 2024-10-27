package com.ssafy.runit.domain.user.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.user.dto.UserInfoResponse;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController implements UserDocs {

    private final UserRepository userRepository;

    @Override
    @GetMapping("/me")
    public RunItApiResponse<UserInfoResponse> geyMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow();
        UserInfoResponse responseDTO = UserInfoResponse.fromEntity(user);
        return RunItApiResponse.create(responseDTO, "사용자 정보 조회에 성공했습니다.");
    }
}
