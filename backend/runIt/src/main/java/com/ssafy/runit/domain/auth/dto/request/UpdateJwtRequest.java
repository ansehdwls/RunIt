package com.ssafy.runit.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateJwtRequest {
    @NotNull(message = "refreshToken is not null")
    private String refreshToken;
}
