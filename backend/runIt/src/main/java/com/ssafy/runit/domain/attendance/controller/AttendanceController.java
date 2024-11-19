package com.ssafy.runit.domain.attendance.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.attendance.dto.response.DayAttendanceResponse;
import com.ssafy.runit.domain.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController implements AttendanceDocs {

    private final AttendanceService attendanceService;

    @Override
    @GetMapping("/week")
    public RunItApiResponse<List<DayAttendanceResponse>> getWeeklyAttendance(@AuthenticationPrincipal UserDetails userDetails) {
        List<DayAttendanceResponse> result = attendanceService.getWeekAttendance(userDetails.getUsername());
        return RunItApiResponse.create(result, "주간 출석 조회에 성공했습니다.");
    }
}
