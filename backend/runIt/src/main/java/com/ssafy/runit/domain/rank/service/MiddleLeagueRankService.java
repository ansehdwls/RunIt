package com.ssafy.runit.domain.rank.service;

public class MiddleLeagueRankService implements LeagueRankService {

    @Override
    public int[] calculateAdvanceDegradeCounts(int userCount) {
        int advanceCount = (int) Math.ceil(userCount * ADVANCE_PERCENTAGE);
        int degradeCount = (int) Math.floor(userCount * DEGRADE_PERCENTAGE);
        return new int[]{advanceCount, degradeCount, userCount - (advanceCount + degradeCount)};
    }
}
