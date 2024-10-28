package com.ssafy.runit.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateJwtRequest {
    @NotNull(message = "refreshToken is not null")
    private String refreshToken;
}
