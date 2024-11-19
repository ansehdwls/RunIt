package com.ssafy.runit.domain.summary.service;

import org.springframework.transaction.annotation.Transactional;

public interface LeagueSummaryService {

    @Transactional
    void processWeeklySummary();
}
