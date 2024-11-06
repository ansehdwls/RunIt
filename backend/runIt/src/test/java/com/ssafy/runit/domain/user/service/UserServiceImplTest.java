package com.ssafy.runit.domain.user.service;

import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        verify(userRepository).findByUserNumber(anyString());
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
        verify(userRepository).findByUserNumber(anyString());
    }

    @Test
    @DisplayName("등록된 유저들의 Fcm Token을 조회할 수 있습니다.")
    void findAllFcmTokens_Success() {
        List<String> fcmTokens = Arrays.asList("1", "2", "3", "4");
        when(userRepository.findAllFcmTokens()).thenReturn(fcmTokens);
        List<String> getFcmTokens = userService.findAllFcmTokens();
        verify(userRepository).findAllFcmTokens();
        assertEquals(fcmTokens.size(), getFcmTokens.size());
    }
}
