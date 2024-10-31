package com.ssafy.runit.domain.attendance.service;

import com.ssafy.runit.domain.attendance.dto.response.DayAttendanceResponse;

import java.util.List;

public interface AttendanceService {

    List<DayAttendanceResponse> getWeekAttendance(String userNumber);
}
