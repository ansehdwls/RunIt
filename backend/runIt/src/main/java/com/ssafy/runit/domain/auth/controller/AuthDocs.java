package com.ssafy.runit.domain.auth.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth API", description = "사용자 인증 관련 API")
public interface AuthDocs {

    @Operation(summary = "사용자 회원가입 API", description = "사용자 회원가입")
    @ApiResponse(responseCode = "200", description = "회원가입에 성공했습니다", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    @ApiResponse(responseCode = "AUTH-004", description = "데이터 형식을 확인해주세요", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "AUTH-005", description = "이미 가입된 사용자입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    RunItApiResponse<Void> register(@RequestBody UserRegisterRequest userRegisterRequest);

}
