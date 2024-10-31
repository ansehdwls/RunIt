package com.ssafy.runit.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserLoginRequest {
    @NotNull
    private String userNumber;

    @Schema(hidden = true)
    public boolean isValid() {
        return userNumber != null && !userNumber.isEmpty();
    }
}
