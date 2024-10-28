package com.ssafy.runit.domain.user.repository;

import com.ssafy.runit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserEmail(String email);

    Optional<User> findByUserEmail(String email);

    @Query("SELECT u.fcmToken FROM User u WHERE u.fcmToken IS NOT NULL")
    List<String> findAllFcmTokens();
}