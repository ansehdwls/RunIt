package com.ssafy.runit.domain.experience.repository;

import com.ssafy.runit.domain.experience.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    List<Experience> findByUser_Id(Long userId);
}
