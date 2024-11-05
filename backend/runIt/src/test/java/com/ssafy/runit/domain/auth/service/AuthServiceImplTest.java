package com.ssafy.runit.domain.auth.service;

import com.ssafy.runit.config.security.CustomUserDetailsService;
import com.ssafy.runit.domain.auth.dto.request.UpdateJwtRequest;
import com.ssafy.runit.domain.auth.dto.request.UserLoginRequest;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.auth.dto.response.LoginResponse;
import com.ssafy.runit.domain.auth.entity.RefreshToken;
import com.ssafy.runit.domain.auth.repository.RefreshTokenRepository;
import com.ssafy.runit.domain.auth.service.AuthServiceImpl;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.league.entity.League;
import com.ssafy.runit.domain.rank.LeagueRank;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.GroupErrorCode;
import com.ssafy.runit.util.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    private final String testNumber = "1234";
    private final String refreshToken = "refresh_token";
    private final String accessToken = "access_token";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Test
    @DisplayName("회원가입 성공 테스트")
    void registerUser_Success() {
        UserRegisterRequest request = UserRegisterRequest
                .builder()
                .userNumber(testNumber)
                .userImageUrl("image")
                .userName("test").build();
        LeagueRank rank = LeagueRank.RANK_1;
        League league = League.builder().leagueName(rank.getLeagueName()).rank(rank).build();
        Group group = Group.builder().groupLeague(league).build();
        User user = request.Mapper(group);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(groupRepository.findDefaultGroup()).thenReturn(Optional.of(group));
        authService.registerUser(request);
        verify(userRepository).save(any(User.class));
        verify(groupRepository).findDefaultGroup();
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("중복된 회원가입이 진행될 경우 에러를 반환합니다.")
    void registerUser_DuplicateUser_ThrowsException() {
        UserRegisterRequest request = UserRegisterRequest
                .builder()
                .userNumber(testNumber)
                .userImageUrl("image")
                .userName("test").build();
        when(userRepository.existsByUserNumber(testNumber)).thenReturn(true);
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.registerUser(request);
        });
        assertEquals(AuthErrorCode.DUPLICATED_USER_ERROR, exception.getErrorCodeType());
        verify(userRepository).existsByUserNumber(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 요청 유효성 검사")
    void registerUser_Invalid_Data_Form_ThrowsException() {
        UserRegisterRequest request = UserRegisterRequest
                .builder()
                .userNumber(null)
                .userImageUrl(null)
                .userName(null).build();
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.registerUser(request);
        });
        assertEquals(AuthErrorCode.INVALID_DATA_FORM, exception.getErrorCodeType());
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).existsByUserNumber(anyString());
        verify(groupRepository, never()).findDefaultGroup();
    }

    @Test
    @DisplayName("로그인 요청 유효성 검사")
    void loginUser_Invalid_Data_Form_ThrowsException() {
        UserLoginRequest request = new UserLoginRequest(null);
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.login(request);
        });
        assertEquals(AuthErrorCode.INVALID_DATA_FORM, exception.getErrorCodeType());
        assertEquals(AuthErrorCode.INVALID_DATA_FORM.message(), exception.getErrorCodeType().message());
        verify(customUserDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    @DisplayName("유저 로그인 성공 테스트")
    void userLoginSuccessTest() {
        UserLoginRequest request = new UserLoginRequest(testNumber);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(testNumber)
                .password(anyString())
                .authorities(new ArrayList<>())
                .build();
        User user = User.builder()
                .id(1L)
                .userNumber(testNumber)
                .build();
        when(customUserDetailsService.loadUserByUsername(testNumber)).thenReturn(userDetails);
        when(jwtTokenProvider.generateAccessToken(testNumber)).thenReturn(accessToken);
        when(jwtTokenProvider.generateRefreshToken(testNumber)).thenReturn(refreshToken);
        when(userRepository.findByUserNumber(testNumber)).thenReturn(Optional.of(user));
        LoginResponse response = authService.login(request);
        assertNotNull(response);
        assertEquals(TOKEN_PREFIX + accessToken, response.accessToken());
        assertEquals(TOKEN_PREFIX + refreshToken, response.refreshToken());
        System.out.println("[예상] refreshToken: " + TOKEN_PREFIX + refreshToken);
        System.out.println("[실제] refreshToken: " + response.refreshToken());
        System.out.println("[예상] accessToken: " + TOKEN_PREFIX + accessToken);
        System.out.println("[실제] accessToken: " + response.accessToken());
        verify(customUserDetailsService).loadUserByUsername(testNumber);
        verify(jwtTokenProvider).generateAccessToken(testNumber);
        verify(jwtTokenProvider).generateRefreshToken(testNumber);
        verify(userRepository).findByUserNumber(testNumber);
    }
}