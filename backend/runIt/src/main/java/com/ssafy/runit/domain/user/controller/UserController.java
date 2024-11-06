package com.ssafy.runit.domain.user.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.user.dto.request.FcmTokenRequest;
import com.ssafy.runit.domain.user.dto.response.UserInfoResponse;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController implements UserDocs {

    private final UserService userService;

    @Override
    @GetMapping("/me")
    public RunItApiResponse<UserInfoResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUserNumber(userDetails.getUsername());
        UserInfoResponse responseDTO = UserInfoResponse.fromEntity(user);
        return RunItApiResponse.create(responseDTO, "사용자 정보 조회에 성공했습니다.");
    }

    @Override
    @PatchMapping("/fcmToken")
    public RunItApiResponse<Void> saveFcmToken(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody FcmTokenRequest request) {
        userService.saveFcmToken(userDetails.getUsername(), request.getFcmToken());
        return RunItApiResponse.create(null, "FcmToken이 저장되었습니다.");
    }
}
