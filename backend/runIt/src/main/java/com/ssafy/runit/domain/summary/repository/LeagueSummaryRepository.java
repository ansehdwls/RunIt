package com.ssafy.runit.domain.summary.repository;

import com.ssafy.runit.domain.summary.entity.LeagueSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeagueSummaryRepository extends JpaRepository<LeagueSummary, Long> {

    Optional<LeagueSummary> findByLeagueId(Long leagueId);
}
