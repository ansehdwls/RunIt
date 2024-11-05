package com.ssafy.runit.domain.rank.service;

public class TopLeagueRankService implements LeagueRankService {

    @Override
    public int[] calculateAdvanceDegradeCounts(int userCount) {
        int degradeCount = (int) Math.floor(userCount * DEGRADE_PERCENTAGE);
        return new int[]{0, degradeCount, userCount - degradeCount};
    }
}
