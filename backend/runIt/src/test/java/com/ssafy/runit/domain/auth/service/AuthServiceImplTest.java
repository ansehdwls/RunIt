package com.ssafy.runit.domain.auth.service;

import com.ssafy.runit.config.security.CustomUserDetailsService;
import com.ssafy.runit.domain.auth.dto.request.UpdateJwtRequest;
import com.ssafy.runit.domain.auth.dto.request.UserLoginRequest;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.auth.dto.response.LoginResponse;
import com.ssafy.runit.domain.auth.entity.RefreshToken;
import com.ssafy.runit.domain.auth.repository.RefreshTokenRepository;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Test
    @DisplayName("기본 그룹이 없을 경우 회원가입 실패 테스트")
    void registerUser_NoDefaultGroup_ThrowsException() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .userNumber(testNumber)
                .userImageUrl("image")
                .userName("test")
                .build();
        when(userRepository.existsByUserNumber(testNumber)).thenReturn(false);
        when(groupRepository.findDefaultGroup()).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.registerUser(request);
        });
        assertEquals(GroupErrorCode.GROUP_NOT_FOUND_ERROR, exception.getErrorCodeType());
        assertEquals(GroupErrorCode.GROUP_NOT_FOUND_ERROR.message(), exception.getErrorCodeType().message());
        verify(groupRepository).findDefaultGroup();
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("리프레시 토큰 저장 - 기존 토큰 업데이트")
    void saveRefreshToken_UpdateToken_Success() {
        User user = User.builder()
                .id(1L)
                .userNumber(testNumber)
                .build();
        RefreshToken existingToken = RefreshToken.builder()
                .id(1L)
                .refreshToken("old_refresh_token")
                .user(user)
                .build();

        when(userRepository.findByUserNumber(testNumber)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUserId(user.getId())).thenReturn(Optional.of(existingToken));
        when(refreshTokenRepository.save(existingToken)).thenReturn(existingToken);
        authService.saveRefreshToken(testNumber, refreshToken);
        assertEquals(refreshToken, existingToken.getRefreshToken());
        verify(userRepository).findByUserNumber(testNumber);
        verify(refreshTokenRepository).findByUserId(user.getId());
        verify(refreshTokenRepository).save(existingToken);
    }

    @Test
    @DisplayName("JWT 토큰 생성 성공 테스트")
    void createJwtToken_Success() {
        String newAccessToken = "new_access_token";
        String newRefreshToken = "new_refresh_token";
        User user = User.builder()
                .id(1L)
                .userNumber(testNumber)
                .build();
        when(jwtTokenProvider.generateAccessToken(testNumber)).thenReturn(newAccessToken);
        when(jwtTokenProvider.generateRefreshToken(testNumber)).thenReturn(newRefreshToken);
        when(userRepository.findByUserNumber(testNumber)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(null);
        LoginResponse response = authService.createJwtToken(testNumber);
        assertNotNull(response);
        assertEquals(TOKEN_PREFIX + newAccessToken, response.accessToken());
        assertEquals(TOKEN_PREFIX + newRefreshToken, response.refreshToken());
        verify(jwtTokenProvider).generateAccessToken(anyString());
        verify(jwtTokenProvider).generateRefreshToken(anyString());
        verify(userRepository).findByUserNumber(testNumber);
        verify(refreshTokenRepository).findByUserId(user.getId());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("리프래시 토큰 갱신 성공 테스트")
    void getNewRefreshToken_Success() {
        UpdateJwtRequest request = new UpdateJwtRequest(refreshToken);
        String newRefreshToken = "new_refresh_token";
        User user = User.builder()
                .id(1L)
                .userNumber(testNumber)
                .build();
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.extractUserNumber(anyString())).thenReturn(testNumber);
        when(jwtTokenProvider.generateAccessToken(anyString())).thenReturn(accessToken);
        when(jwtTokenProvider.generateRefreshToken(anyString())).thenReturn(newRefreshToken);
        when(userRepository.findByUserNumber(anyString())).thenReturn(Optional.of(user));
        newRefreshToken = authService.getNewRefreshToken(request).refreshToken();
        System.out.println("[갱신 전]: " + TOKEN_PREFIX + refreshToken);
        System.out.println("[갱신 후]: " + newRefreshToken);
        assertNotEquals(refreshToken, newRefreshToken);
        verify(jwtTokenProvider).validateToken(anyString());
        verify(jwtTokenProvider).extractUserNumber(anyString());
        verify(jwtTokenProvider).generateAccessToken(anyString());
        verify(jwtTokenProvider).generateRefreshToken(anyString());
    }

    @Test
    @DisplayName("리프레시 토큰 저장 - 등록되지 않은 사용자 예외")
    void saveRefreshToken_UnregisteredUser_ThrowsException() {
        when(userRepository.findByUserNumber(testNumber)).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.saveRefreshToken(testNumber, refreshToken);
        });
        assertEquals(AuthErrorCode.UNREGISTERED_USER_ERROR, exception.getErrorCodeType());
        assertEquals(AuthErrorCode.UNREGISTERED_USER_ERROR.message(), exception.getErrorCodeType().message());
        verify(userRepository).findByUserNumber(testNumber);
        verify(refreshTokenRepository, never()).findByUserId(anyLong());
        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("리프래시 토큰이 만료된 경우 에러를 반환합니다.")
    void getNewRefreshToken_InvalidRefreshToken_ThrowsException() {
        UpdateJwtRequest request = new UpdateJwtRequest(refreshToken);
        when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(false);
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.getNewRefreshToken(request);
        });
        assertEquals(exception.getErrorCodeType(), AuthErrorCode.EXPIRED_TOKEN_ERROR);
        assertEquals(AuthErrorCode.EXPIRED_TOKEN_ERROR.message(), exception.getErrorCodeType().message());
    }
}