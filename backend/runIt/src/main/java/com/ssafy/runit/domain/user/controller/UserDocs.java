package com.ssafy.runit.domain.user.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.user.dto.UserInfoResponse;
import com.ssafy.runit.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Tag(name = "User API", description = "사용자 관련 API")
public interface UserDocs {
    @Operation(summary = "사용자 정보 조회 API", description = "사용자 정보 조회")
    @ApiResponse(responseCode = "200", description = "정보조회에 성공했습니다", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    @ApiResponse(responseCode = "AUTH-006", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    RunItApiResponse<UserInfoResponse> geyMyInfo(@AuthenticationPrincipal UserDetails userDetails);
}
