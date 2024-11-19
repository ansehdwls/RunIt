package com.ssafy.runit.domain.user.service;

import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private final String TEST_NUMBER = "1234";


    @Test
    @DisplayName("등록된 유저를 조회할 수 있습니다.")
    void findByUserNumber_Success() {
        User user = User.builder()
                .id(1L)
                .userNumber(TEST_NUMBER)
                .build();
        when(userRepository.findByUserNumber(TEST_NUMBER)).thenReturn(Optional.of(user));
        User findUser = userService.findByUserNumber(TEST_NUMBER);
        verify(userRepository).findByUserNumber(eq(TEST_NUMBER));
        assertNotNull(findUser);
        assertEquals(TEST_NUMBER, findUser.getUserNumber());
    }


    @Test
    @DisplayName("등록되지 않는 유저를 조회할 경우 에러를 반환합니다.")
    void findByUserNumber_NotFound() {
        when(userRepository.findByUserNumber(TEST_NUMBER)).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.findByUserNumber(TEST_NUMBER);
        });
        assertEquals(AuthErrorCode.UNREGISTERED_USER_ERROR, exception.getErrorCodeType());
        verify(userRepository).findByUserNumber(eq(TEST_NUMBER));
    }

    @Test
    @DisplayName("등록된 유저들의 Fcm Token을 조회할 수 있습니다.")
    void findAllFcmTokens_Success() {
        List<String> fcmTokens = Arrays.asList("1", "2", "3", "4");
        when(userRepository.findAllFcmTokens()).thenReturn(fcmTokens);
        List<String> getFcmTokens = userService.findAllFcmTokens();
        verify(userRepository, times(1)).findAllFcmTokens();
        assertEquals(fcmTokens.size(), getFcmTokens.size());
        assertEquals(fcmTokens, getFcmTokens);
    }

    @Test
    @DisplayName("모든 Fcm Token을 성공적으로 조회할 수 있습니다.")
    void findAllFcmTokens_EmptyList() {
        List<String> fcmTokens = List.of();
        when(userRepository.findAllFcmTokens()).thenReturn(fcmTokens);
        List<String> getFcmTokens = userService.findAllFcmTokens();
        verify(userRepository, times(1)).findAllFcmTokens();
        assertNotNull(getFcmTokens);
        assertTrue(getFcmTokens.isEmpty());
    }

    @Test
    @DisplayName("등록된 유저들의 Fcm Token을 갱신할 수 있습니다.")
    void saveFcmToken_Success() {
        User user = User.builder()
                .id(1L)
                .fcmToken("fcmToken")
                .userNumber(TEST_NUMBER)
                .build();
        when(userRepository.findByUserNumber(TEST_NUMBER)).thenReturn(Optional.of(user));
        userService.saveFcmToken(TEST_NUMBER, "newFcmToken");
        verify(userRepository, times(1)).findByUserNumber(eq(TEST_NUMBER));
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> fcmTokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(1)).updateFcmTokenByUserId(userIdCaptor.capture(), fcmTokenCaptor.capture());
        assertEquals(user.getId(), userIdCaptor.getValue(), "User ID가 일치해야 합니다.");
        assertEquals("newFcmToken", fcmTokenCaptor.getValue(), "FCM Token이 갱신되어야 합니다.");
    }

    @Test
    @DisplayName("등록되지 않은 유저의 Fcm Token 갱신 시 예외가 발생합니다.")
    void saveFcmToken_UserNotFound() {
        when(userRepository.findByUserNumber(TEST_NUMBER)).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saveFcmToken(TEST_NUMBER, "newFcmToken");
        });
        verify(userRepository, times(1)).findByUserNumber(eq(TEST_NUMBER));
        verify(userRepository, never()).updateFcmTokenByUserId(anyLong(), anyString());
        assertEquals(AuthErrorCode.UNREGISTERED_USER_ERROR, exception.getErrorCodeType());
    }
}
