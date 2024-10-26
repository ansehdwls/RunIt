package com.ssafy.runit.domain.auth.repository;

import com.ssafy.runit.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserEmail(String email);

    Optional<User> findByUserEmail(String email);
}
