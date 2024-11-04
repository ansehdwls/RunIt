package com.ssafy.runit.domain.rank.service;

public class NewbieLeagueRankService implements LeagueRankService {
    @Override
    public int[] calculateAdvanceDegradeCounts(int userCount) {
        return new int[]{userCount, 0, 0};
    }
}
