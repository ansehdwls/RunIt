package com.ssafy.runit.domain.league.repository;

import com.ssafy.runit.domain.league.LeagueRank;
import com.ssafy.runit.domain.league.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Long> {

    List<League> findAllByOrderByRankAsc();

    Optional<League> findFirstByRankGreaterThanOrderByRankAsc(LeagueRank rank);

    Optional<League> findFirstByRankLessThanOrderByRankDesc(LeagueRank rank);

    @Query("SELECT l FROM League l JOIN FETCH l.groups WHERE l.rank = :league_rank")
    League findLeagueWithGroups(@Param("league_rank") LeagueRank leagueRank);
}
