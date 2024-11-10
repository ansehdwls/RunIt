package com.ssafy.runit.domain.record.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Tag(name = "Record Api", description = "러닝 관련 api")
public interface RecordDocs {
    @Operation(summary = "러닝 저장 Api", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 기록 저장", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<RecordPostResponse> saveRecord(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestPart(value = "dto") RecordSaveRequest recordSaveRequest,
                                                    @RequestPart(value = "images", required = false) MultipartFile file);

    @Operation(summary = "러닝 개별 기록 호출 Api", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 개별 기록 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<RecordGetResponse> recordFindOne(@AuthenticationPrincipal UserDetails userDetails, @PathVariable(name="recordId") Long recordId);

    @Operation(summary = "러닝 전체 기록 호출 Api", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 기록 리스트 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<List<RecordGetListResponse>> recordFindList(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "러닝 전체 기록 호출 Api (연습)", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 기록 리스트 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<List<RecordGetListResponse>> recordFindPractiseList(@AuthenticationPrincipal UserDetails userDetails);


    @Operation(summary = "러닝 당일 기록 호출 Api", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 기록 리스트 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<RecordTodayResponse> recordFindToday(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "러닝 주 기록 호출 Api", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 기록 리스트 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<RecordGetWeekResponse> recordFindWeek(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "러닝 전체 기록 호출 Api", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 기록 리스트 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<RecordGetTotalResponse> recordFindTotal(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "러닝 주간 리스트 호출 Api", description = "러닝")
    @ApiResponse(responseCode = "200", description = "러닝 기록 리스트 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<List<RecordGetCalendarResponse>> recordFindWeekList(@AuthenticationPrincipal UserDetails userDetails, @PathVariable(name = "today") LocalDate today);


}
