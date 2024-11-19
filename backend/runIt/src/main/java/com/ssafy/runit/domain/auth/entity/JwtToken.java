package com.ssafy.runit.domain.auth.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@AllArgsConstructor
@Getter
@RedisHash(value = "jwtToken")
public class JwtToken {

    @Id
    private String id;

    private String refreshToken;

    @TimeToLive
    private Long expiresIn;
}
