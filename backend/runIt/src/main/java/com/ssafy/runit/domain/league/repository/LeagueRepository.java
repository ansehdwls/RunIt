package com.ssafy.runit.domain.league.repository;

import com.ssafy.runit.domain.league.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Long> {

    List<League> findAllByOrderByRankAsc();

    Optional<League> findFirstByRankGreaterThanOrderByRankAsc(long rank);

    Optional<League> findFirstByRankLessThanOrderByRankDesc(long rank);
}
