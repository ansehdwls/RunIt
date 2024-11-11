package com.ssafy.runit.domain.user.repository;

import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query("SELECT u FROM User u JOIN u.experiences e " +
            "WHERE u.userGroup.id = :groupId AND e.createAt >= :startDate " +
            "GROUP BY u.id " +
            "ORDER BY SUM(e.changed) ASC")
    List<User> findUsersWithExperienceSum(@Param("groupId") Long groupId, @Param("startDate") LocalDateTime startDate);

    List<User> findUserByUserGroup(Group group);
}
