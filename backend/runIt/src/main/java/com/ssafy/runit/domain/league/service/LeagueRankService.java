package com.ssafy.runit.domain.league.service;

public interface LeagueRankService {

    double ADVANCE_PERCENTAGE = 0.3;
    double DEGRADE_PERCENTAGE = 0.3;

    int[] calculateAdvanceDegradeCounts(int userCount);
}

