package com.ssafy.runit.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FcmTokenRequest {

    @NotNull
    private String fcmToken;
}
