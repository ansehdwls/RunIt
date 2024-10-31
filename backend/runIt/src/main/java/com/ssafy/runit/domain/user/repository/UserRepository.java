package com.ssafy.runit.domain.user.repository;

import com.ssafy.runit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserNumber(String userNumber);

    Optional<User> findByUserNumber(String userNumber);

    @Modifying
    @Query("UPDATE User u SET u.fcmToken = :fcmToken WHERE u.id = :userId")
    void updateFcmTokenByUserId(@Param("userId") Long userId, @Param("fcmToken") String fcmToken);

    @Query("SELECT u.fcmToken FROM User u WHERE u.fcmToken IS NOT NULL")
    List<String> findAllFcmTokens();
}