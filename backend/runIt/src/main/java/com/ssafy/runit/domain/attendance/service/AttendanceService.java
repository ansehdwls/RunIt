package com.ssafy.runit.domain.attendance.service;

import com.ssafy.runit.domain.attendance.dto.response.DayAttendanceResponse;
import com.ssafy.runit.domain.attendance.entity.Attendance;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceService {

    List<DayAttendanceResponse> getWeekAttendance(String userNumber);

    Boolean getTodayAttended(LocalDateTime today);

    Void saveAttendance(Attendance attendance);
}
