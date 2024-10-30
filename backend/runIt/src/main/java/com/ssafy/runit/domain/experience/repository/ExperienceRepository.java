package com.ssafy.runit.domain.experience.repository;

import com.ssafy.runit.domain.experience.dto.response.ExperienceGetListResponse;
import com.ssafy.runit.domain.experience.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    List<ExperienceGetListResponse> findByUser_Id(Long userId);

    @Query("SELECT e.user.id, SUM(e.changed) FROM Experience e " +
            "WHERE e.user.id IN :userIds AND e.createAt >= :startDate " +
            "GROUP BY e.user.id")
    List<Long[]> sumExperienceByUserIdsAndStartDate(@Param("userIds") List<Long> userIds, @Param("startDate") LocalDateTime startDate);
}
