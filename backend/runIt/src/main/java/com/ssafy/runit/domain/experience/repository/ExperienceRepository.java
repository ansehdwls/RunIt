package com.ssafy.runit.domain.experience.repository;

import com.ssafy.runit.domain.experience.dto.response.ExperienceGetListResponse;
import com.ssafy.runit.domain.experience.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query("SELECT SUM(e.changed) FROM Experience e " +
            "WHERE e.user.id IN :userId AND e.createAt >= :startDate")
    Long experienceChangedSum(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    List<ExperienceGetListResponse> findByUser_Id(Long userId);

    List<Experience> findByUserIdAndCreateAtBetween(Long userId, LocalDateTime startDay, LocalDateTime endDay);
}
