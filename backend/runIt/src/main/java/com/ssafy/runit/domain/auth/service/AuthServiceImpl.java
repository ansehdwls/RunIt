package com.ssafy.runit.domain.auth.service;

import com.ssafy.runit.config.security.CustomUserDetailsService;
import com.ssafy.runit.domain.auth.dto.request.UpdateJwtRequest;
import com.ssafy.runit.domain.auth.dto.request.UserLoginRequest;
import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.auth.dto.response.LoginResponse;
import com.ssafy.runit.domain.auth.entity.JwtToken;
import com.ssafy.runit.domain.auth.repository.JwtTokenRepository;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.rank.service.RankService;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.GroupErrorCode;
import com.ssafy.runit.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenRepository jwtTokenRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RankService rankService;
    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.refresh-token-expiration:3600000}")
    private Long refreshTokenExpiration;


    @Override
    @Transactional
    public void registerUser(UserRegisterRequest request) {
        if (!request.isValid()) {
            log.error("잘못된 입력 형식 : email:{} name : {} image:{}", request.getUserNumber(), request.getUserName(), request.getUserImageUrl());
            throw new CustomException(AuthErrorCode.INVALID_DATA_FORM);
        }

        if (existsByUserNumber(request.getUserNumber())) {
            log.error("가입된 사용자 : email : {}", request.getUserNumber());
            throw new CustomException(AuthErrorCode.DUPLICATED_USER_ERROR);
        }
        Group group = groupRepository.findDefaultGroup().orElseThrow(
                () -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND_ERROR)
        );
        User user = request.Mapper(group);
        user = userRepository.save(user);
        rankService.updateScore(group.getId(), String.valueOf(user.getId()), 0);
    }

    @Override
    @Transactional
    public LoginResponse createJwtToken(String userNumber) {
        String refreshToken = TOKEN_PREFIX + jwtTokenProvider.generateRefreshToken(userNumber);
        String accessToken = TOKEN_PREFIX + jwtTokenProvider.generateAccessToken(userNumber);
        saveRefreshToken(userNumber, refreshToken, accessToken);
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public boolean existsByUserNumber(String userNumber) {
        return userRepository.existsByUserNumber(userNumber);
    }


    @Override
    @Transactional
    public LoginResponse login(UserLoginRequest request) {
        if (!request.isValid()) {
            log.error("잘못된 입력 형식 : {}", request.getUserNumber());
            throw new CustomException(AuthErrorCode.INVALID_DATA_FORM);
        }
        String userEmail = request.getUserNumber();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return createJwtToken(userEmail);
    }

    @Override
    @Transactional
    public void saveRefreshToken(String userNumber, String refreshToken, String accessToken) {
        userRepository.findByUserNumber(userNumber).orElseThrow(
                () -> new CustomException(AuthErrorCode.UNREGISTERED_USER_ERROR)
        );
        String tokenWithoutBearer = refreshToken.replaceFirst("^Bearer ", "");
        jwtTokenRepository.save(new JwtToken(userNumber, tokenWithoutBearer, refreshTokenExpiration));
    }

    @Override
    @Transactional
    public LoginResponse getNewRefreshToken(UpdateJwtRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(AuthErrorCode.EXPIRED_TOKEN_ERROR);
        }

        String userNumber = jwtTokenProvider.extractUserNumber(refreshToken);
        JwtToken storedToken = jwtTokenRepository.findById(userNumber).orElseThrow(
                () -> new CustomException(AuthErrorCode.EXPIRED_TOKEN_ERROR)
        );
        if (!refreshToken.equals(storedToken.getRefreshToken())) {
            throw new CustomException(AuthErrorCode.INVALID_TOKEN_ERROR);
        }
        return createJwtToken(userNumber);
    }
}
