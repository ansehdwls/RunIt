package com.ssafy.runit.domain.attendance.service;

import com.ssafy.runit.domain.attendance.dto.AttendanceSaveDto;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
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
    public Boolean getTodayAttended(UserDetails userDetails, LocalDate today) {
        User user = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();
        /*
        * 반환 값이 널이면은 해당 날은 출석을 안했다는 거니까
        * -> false
        *
        * 반환 값이 있으면 해당 날은 출석을 했다는 거니까
        * -> true
        *
        * .orElse -> 값이 있을 경우 true, 없을 경우 false
        *
        * */
        return attendanceRepository.findByUserAndCreatedAt(user, today)
                .map(attendance -> {

                    saveAttendance(userDetails);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Void saveAttendance(UserDetails userDetails) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();
        Attendance attendance = new AttendanceSaveDto().toEntity(findUser);
        attendanceRepository.save(attendance);
        return null;
    }


}
