package com.ssafy.runit.domain.attendance.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DayAttendanceResponse(
        String day,
        boolean attended,
        LocalDate date
) {

    public static DayAttendanceResponse fromEntity(String day, LocalDate date, boolean attended) {
        return DayAttendanceResponse.builder()
                .day(day)
                .attended(attended)
                .date(date)
                .build();
    }
}
