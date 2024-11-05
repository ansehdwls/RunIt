package com.ssafy.runit.domain.pace.repository;


import com.ssafy.runit.domain.pace.entity.Pace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaceRepository extends JpaRepository<Pace, Long> {
}
