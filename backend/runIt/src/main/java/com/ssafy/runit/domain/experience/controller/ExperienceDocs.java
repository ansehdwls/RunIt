package com.ssafy.runit.domain.experience.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Experience Api", description = "경험치 관련 api")
public interface ExperienceDocs {
    @Operation(summary = "경험치 api", description = "경험치")
    @ApiResponse(responseCode = "200", description = "경험치 저장", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<Void> saveExperience(@RequestBody ExperienceSaveRequest experienceSaveRequest);

}
