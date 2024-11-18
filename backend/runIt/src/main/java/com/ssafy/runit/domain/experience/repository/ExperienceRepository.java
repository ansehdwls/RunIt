package com.ssafy.runit.domain.experience.repository;

import com.ssafy.runit.domain.experience.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query("SELECT SUM(e.changed) FROM Experience e " +
            "WHERE e.user.id IN :userId AND e.createAt >= :startDate")
    Long experienceChangedSum(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    List<Experience> findByUserId(Long userId);

    @Query("SELECT sum(e.changed) from Experience e " +
            "WHERE e.user.id = :userId " +
            "AND e.activity = '거리' " +
            "AND e.createAt BETWEEN :startDay AND :endDay")
    Optional<Long> findByUserIdAndCreateAtBetween(@Param("userId") Long userId, @Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

    List<Experience> findByUserIdOrderByCreateAtDesc(Long userId);
}
