package com.ssafy.runit.domain.experience.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.entity.Experience;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Experience Api", description = "경험치 관련 api")
public interface ExperienceDocs {
    @Operation(summary = "경험치 api", description = "경험치")
    @ApiResponse(responseCode = "200", description = "경험치 저장", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<Void> saveExperience(@RequestBody ExperienceSaveRequest experienceSaveRequest);


    @Operation(summary = "경험치 api", description = "경험치 조회 api")
    @ApiResponse(responseCode = "200", description = "경험치 조회", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<List<Experience>> getListExperience(@RequestParam(required = true) Long userId);

    @Operation(summary = "경험치 api", description = "경험치 조회 api")
    @ApiResponse(responseCode = "200", description = "경험치 합계", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<Long> getWeekSumExperience(@RequestParam(required = true) Long userId);


}
