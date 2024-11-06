package com.ssafy.runit.domain.record.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.RecordGetListResponse;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Tag(name = "Record Api", description = "러닝 관련 api")
public interface RecordDocs {
    @Operation(summary = "러닝 저장 Api", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 기록 저장", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<Void> saveRecord(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestPart(value = "dto") RecordSaveRequest recordSaveRequest,
                                      @RequestPart(value = "images", required = false) MultipartFile file);

    @Operation(summary = "러닝 개별 기록 호출 Api", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 개별 기록 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<RecordGetResponse> recordFindOne(@AuthenticationPrincipal UserDetails userDetails, @PathVariable(name="recordId") Long recordId);

    @Operation(summary = "러닝 전체 기록 호출 Api", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 기록 리스트 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<List<RecordGetListResponse>> recordFindList(@AuthenticationPrincipal UserDetails userDetails);


}
