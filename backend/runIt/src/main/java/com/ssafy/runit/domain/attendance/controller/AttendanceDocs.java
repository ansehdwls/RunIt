package com.ssafy.runit.domain.attendance.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.attendance.dto.response.DayAttendanceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Tag(name = "Attendance API", description = "출석 관련 API")
public interface AttendanceDocs {

    @Operation(summary = "출석 조회 관련 API", description = "주간 출석 조회")
    @ApiResponse(responseCode = "200", description = "주간 출석 조회에 성공했습니다", content = @Content(schema = @Schema(implementation = RunItApiResponse.class)))
    RunItApiResponse<List<DayAttendanceResponse>> getWeeklyAttendance(UserDetails userDetails);
}
