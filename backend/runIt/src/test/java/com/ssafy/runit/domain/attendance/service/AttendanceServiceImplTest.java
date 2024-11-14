package com.ssafy.runit.domain.attendance.service;

import com.ssafy.runit.config.security.CustomUserDetails;
import com.ssafy.runit.domain.attendance.entity.Attendance;
import com.ssafy.runit.domain.attendance.repository.AttendanceRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceImplTest {

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserRepository userRepository;

    private static User user;
    private static final String TEST_NUMBER = "1234";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .userNumber(TEST_NUMBER)
                .build();
    }


    @Test
    @DisplayName("출석 저장 성공 테스트")
    void saveAttendance_Success() {
        UserDetails userDetails = new CustomUserDetails(user);
        when(userRepository.findByUserNumber(eq(TEST_NUMBER))).thenReturn(Optional.of(user));
        attendanceService.saveAttendance(userDetails);
        verify(userRepository, times(1)).findByUserNumber(eq(TEST_NUMBER));
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    @DisplayName("출석 저장 실패 - 사용자 미존재 시 예외 발생")
    void saveAttendance_UnregisteredUser_ThrowException() {
        UserDetails userDetails = new CustomUserDetails(user);
        when(userRepository.findByUserNumber(eq(TEST_NUMBER))).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> attendanceService.saveAttendance(userDetails));
        assertEquals(AuthErrorCode.UNREGISTERED_USER_ERROR, exception.getErrorCodeType());
        assertEquals(AuthErrorCode.UNREGISTERED_USER_ERROR.message(), exception.getErrorCodeType().message());
        verify(attendanceRepository, never()).save(any(Attendance.class));
        verify(userRepository, times(1)).findByUserNumber(eq(TEST_NUMBER));
    }
}
