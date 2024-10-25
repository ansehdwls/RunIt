package com.ssafy.runit.domain.experience.repository;

import com.ssafy.runit.domain.experience.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query(value = "select sum(e.changed) from experience e where user_id = :id ", nativeQuery = true)
    Long experienceChangedSum(@Param("id") Long id);

    List<Experience> findByUser_Id(Long userId);
}
