package com.ssafy.runit.domain.attendance.dto;

import com.ssafy.runit.domain.attendance.entity.Attendance;
import com.ssafy.runit.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSaveDto {
    private LocalDate createAt;

    public Attendance toEntity(User user){
        return Attendance.builder()
                .user(user)
                .createdAt(LocalDate.now())
                .build();
    }
}
