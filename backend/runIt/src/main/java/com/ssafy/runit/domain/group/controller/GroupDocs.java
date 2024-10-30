package com.ssafy.runit.domain.group.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.group.dto.response.GetGroupUsersResponse;
import com.ssafy.runit.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Group API", description = "그룹 관련 API")
public interface GroupDocs {

    @Operation(summary = "그룹 구성원 정보 조회 API", description = "그룹 유저 정보 조회")
    @ApiResponse(responseCode = "200", description = "그룹 구성원 정보 조회에 성공했습니다.", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    @ApiResponse(responseCode = "GROUP-001", description = "존재하지 않는 그룹입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    RunItApiResponse<List<GetGroupUsersResponse>> GetGroupUsersInfo(@RequestParam Long groupId);
}
