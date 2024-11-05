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
}