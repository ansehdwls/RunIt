package com.ssafy.runit.domain.track.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.track.dto.response.TrackImgResponse;
import com.ssafy.runit.domain.track.dto.response.TrackRouteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Track Api", description = "트랙 관련 api")
public interface TrackDocs {

    @Operation(summary = "트랙 이미지 호출 Api", description = "트랙")
    @ApiResponse(responseCode = "200", description = "트랙 이미지 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<TrackImgResponse> trackFindImg(@PathVariable(name="recordId") Long recordId);

    @Operation(summary = "트랙 경로 호출 Api", description = "트랙")
    @ApiResponse(responseCode = "200", description = "트랙 경로 호출", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<TrackRouteResponse> trackFindRoute(@PathVariable(name="recordId") Long recordId);



}
