package com.ssafy.runit.domain.experience.repository;

import com.ssafy.runit.domain.experience.dto.response.ExperienceGetListResponse;
import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.group.dto.response.GetGroupUsersResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query("SELECT SUM(e.changed) FROM Experience e " +
            "WHERE e.user.id IN :userId AND e.createAt >= :startDate")
    Long experienceChangedSum(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    List<ExperienceGetListResponse> findByUser_Id(Long userId);

    @Query("SELECT new com.ssafy.runit.domain.group.dto.response.GetGroupUsersResponse(u.userName, u.imageUrl, COALESCE(CAST(SUM(e.changed) AS long), 0L)) " +
            "FROM User u LEFT JOIN u.experiences e " +
            "ON e.createAt >= :startDate " +
            "WHERE u.id IN :userIds " +
            "GROUP BY u.id " +
            "ORDER BY COALESCE(SUM(e.changed), 0) DESC")
    List<GetGroupUsersResponse> sumExperienceByUserIdsAndStartDate(@Param("userIds") List<Long> userIds, @Param("startDate") LocalDateTime startDate);
}
