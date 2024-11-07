package com.ssafy.runit.domain.auth.repository;

import com.ssafy.runit.domain.auth.entity.JwtToken;
import org.springframework.data.repository.CrudRepository;

public interface JwtTokenRepository extends CrudRepository<JwtToken, String> {
}
