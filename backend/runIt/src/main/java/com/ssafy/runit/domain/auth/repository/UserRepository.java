package com.ssafy.runit.domain.auth.repository;

import com.ssafy.runit.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserEmail(String email);
}
