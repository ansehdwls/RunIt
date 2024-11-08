package com.ssafy.runit.domain.attendance.service;

import com.ssafy.runit.domain.attendance.dto.response.DayAttendanceResponse;
import com.ssafy.runit.domain.attendance.entity.Attendance;
import com.ssafy.runit.domain.attendance.repository.AttendanceRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Override
    public List<DayAttendanceResponse> getWeekAttendance(String userNumber) {
        User user = userRepository.findByUserNumber(userNumber).orElseThrow(
                () -> new CustomException(AuthErrorCode.UNREGISTERED_USER_ERROR)
        );
        LocalDate nearMonday = DateUtils.getLastMonday();
        List<LocalDate> attendances = attendanceRepository.findByUserAndCreatedAtAfterOrderByCreatedAtAsc(user, nearMonday)
                .stream().map(Attendance::getCreatedAt)
                .toList();
        return Stream.of(DayOfWeek.values()).map(
                day -> {
                    LocalDate targetDate = nearMonday.plusDays(day.getValue() - 1);
                    boolean attended = attendances.contains(targetDate);
                    return DayAttendanceResponse.fromEntity(DateUtils.getDayNameInKorean(day), targetDate, attended);
                }).toList();
    }

    @Override
    public Boolean getTodayAttended(LocalDateTime today) {
//
//        if (attendanceRepository.findByCreatedAt(today) == null){
//            return false;
//        }

        log.debug("today = {}", today);

        return true;
    }

    @Override
    public Void saveAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
        return null;
    }


}
