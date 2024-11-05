package com.ssafy.runit.domain.rank.service;

public interface LeagueRankService {

    double ADVANCE_PERCENTAGE = 0.3;
    double DEGRADE_PERCENTAGE = 0.3;

    int[] calculateAdvanceDegradeCounts(int userCount);
}

