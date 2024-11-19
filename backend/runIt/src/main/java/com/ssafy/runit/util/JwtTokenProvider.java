package com.ssafy.runit.util;

import com.ssafy.runit.config.security.CustomUserDetailsService;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {


    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * accessToken 생성
     *
     * @param userNumber
     * @return accessToken
     */
    public String generateAccessToken(String userNumber) {
        return generateToken(userNumber, accessTokenExpiration);
    }

    /**
     * refreshToken 생성
     *
     * @param userNumber
     * @return refreshToken
     */
    public String generateRefreshToken(String userNumber) {
        return generateToken(userNumber, refreshTokenExpiration);
    }

    /**
     * 토큰 유효성 검사
     *
     * @param token
     * @return true -> 유효함, false -> 만료
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomException(AuthErrorCode.EXPIRED_TOKEN_ERROR);
        } catch (IllegalArgumentException | UnsupportedJwtException e) {
            throw new CustomException(AuthErrorCode.INVALID_TOKEN_ERROR);
        } catch (MalformedJwtException e) {
            throw new CustomException(AuthErrorCode.INVALID_TOKEN_ERROR);
        }
    }


    /**
     * Token 기반 Claims 추출
     *
     * @param token
     * @return
     */
    public Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    /**
     * token으로 user의 email을 가져옴.
     *
     * @param token
     * @return
     */
    public String extractUserNumber(String token) {
        return extractClaims(token).getSubject();
    }


    public Authentication getAuthentication(String token) {
        String userNumber = extractUserNumber(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userNumber);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    /**
     * 토큰 생성
     *
     * @param userNumber
     * @param expiration
     * @return
     */
    public String generateToken(String userNumber, long expiration) {
        return Jwts.builder()
                .issuer("RunIt")
                .subject(userNumber)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + expiration))
                .signWith(secretKey)
                .compact();
    }
}
