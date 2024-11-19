package com.ssafy.runit.domain.league;

import com.ssafy.runit.domain.league.service.*;
import lombok.Getter;

@Getter
public enum LeagueRank {

    RANK_1(1, "알", new NewbieLeagueRankService()),

    RANK_2(2, "나무늘보", new BottomLeagueRankService()),

    RANK_3(3, "거북이", new MiddleLeagueRankService()),

    RANK_4(4, "토끼", new MiddleLeagueRankService()),

    RANK_5(5, "말", new MiddleLeagueRankService()),

    RANK_6(6, "치타", new TopLeagueRankService());

    private final int rank;
    private final String leagueName;
    private final LeagueRankService leagueRankService;

    LeagueRank(int rank, String leagueName, LeagueRankService leagueRankService) {
        this.rank = rank;
        this.leagueName = leagueName;
        this.leagueRankService = leagueRankService;
    }

    public static LeagueRank fromRank(int rank) {
        for (LeagueRank leagueRank : values()) {
            if (leagueRank.rank == rank) {
                return leagueRank;
            }
        }
        throw new IllegalArgumentException("Invalid rank: " + rank);
    }
}