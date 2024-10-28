package com.ssafy.runit.domain.auth.service;

import com.ssafy.runit.config.security.CustomUserDetailsService;
import com.ssafy.runit.domain.auth.dto.request.UserLoginRequest;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.auth.dto.response.LoginResponse;
import com.ssafy.runit.domain.auth.entity.RefreshToken;
import com.ssafy.runit.domain.auth.repository.RefreshTokenRepository;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.GroupErrorCode;
import com.ssafy.runit.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private static final String TOKEN_PREFIX = "Bearer ";


    @Override
    public void registerUser(UserRegisterRequest request) {
        if (!request.isValid()) {
            log.error("잘못된 입력 형식 : email:{} name : {} image:{}", request.getUserEmail(), request.getUserName(), request.getUserImageUrl());
            throw new CustomException(AuthErrorCode.INVALID_DATA_FORM);
        }

        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            log.error("가입된 사용자 : email : {}", request.getUserEmail());
            throw new CustomException(AuthErrorCode.DUPLICATED_USER_ERROR);
        }
        Group group = groupRepository.findDefaultGroup().orElseThrow(
                () -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND_ERROR)
        );
        User user = request.Mapper(group);
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(UserLoginRequest request) {
        if (!request.isValid()) {
            log.error("잘못된 입력 형식 : {}", request.getUserEmail());
            throw new CustomException(AuthErrorCode.INVALID_DATA_FORM);
        }
        String userEmail = request.getUserEmail();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String refreshToken = TOKEN_PREFIX + jwtTokenProvider.generateRefreshToken(userEmail);
        String accessToken = TOKEN_PREFIX + jwtTokenProvider.generateAccessToken(userEmail);
        saveRefreshToken(userEmail, refreshToken);
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public void saveRefreshToken(String userEmail, String refreshToken) {
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new CustomException(AuthErrorCode.UNREGISTERED_USER_ERROR)
        );
        refreshTokenRepository.findByUserId(user.getId())
                .map(existingToken -> {
                    existingToken.setRefreshToken(refreshToken);
                    return refreshTokenRepository.save(existingToken);
                })
                .orElseGet(() -> {
                    RefreshToken newRefreshToken = RefreshToken.builder().refreshToken(refreshToken).user(user).build();
                    return refreshTokenRepository.save(newRefreshToken);
                });
    }
}
