package com.ssafy.runit.domain.league.service;

public class BottomLeagueRankService implements LeagueRankService {

    @Override
    public int[] calculateAdvanceDegradeCounts(int userCount) {
        int advanceCount = (int) Math.ceil(userCount * ADVANCE_PERCENTAGE);
        return new int[]{advanceCount, 0, userCount - advanceCount};
    }
}
