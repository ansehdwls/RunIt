package com.ssafy.runit.domain.league.service;

public class NewbieLeagueRankService implements LeagueRankService {
    @Override
    public int[] calculateAdvanceDegradeCounts(int userCount) {
        return new int[]{userCount, 0, 0};
    }
}
