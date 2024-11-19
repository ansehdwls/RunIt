package com.ssafy.runit.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJwtRequest {
    @NotNull(message = "refreshToken is not null")
    private String refreshToken;
}
