package com.ssafy.runit.summary;

import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.league.entity.League;
import com.ssafy.runit.domain.league.repository.LeagueRepository;
import com.ssafy.runit.domain.summary.entity.LeagueSummary;
import com.ssafy.runit.domain.summary.repository.LeagueSummaryRepository;
import com.ssafy.runit.domain.summary.service.LeagueSummaryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@Transactional // 테스트 시 트랜잭션 롤백을 원할 경우 사용
public class LeagueServicePerformanceTest {

    @Autowired
    private SummaryFactory testDataFactory;

    @Autowired
    private LeagueSummaryService leagueSummaryService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private LeagueSummaryRepository leagueSummaryRepository;

    @BeforeEach
    public void before() {
        System.out.println("Test Start");
    }


    @AfterEach
    public void after() {
        System.out.println("Test End");
    }

    @Test
    @DisplayName("주간 리그 결산 테스트")
    public void weeklyLeagueSummaryTest() {
        // 1.
        int groupsPerLeague = 13;     // 리그당 그룹 개수
        int usersPerGroup = 10;       // 그룹당 유저 개수
        testDataFactory.createLeagues(groupsPerLeague, usersPerGroup);
        System.out.println("테스트 데이터 생성");
        // 2. 성능 측정 시작
        long startTime = System.currentTimeMillis();
        System.out.println("processWeeklySummary Start");
        // 3. 주간 요약 처리 메서드 호출
        leagueSummaryService.processWeeklySummary();
        // 4. 성능 측정 종료
        List<League> leagues = leagueRepository.findAll();
        for (League league : leagues) {
            long currentRank = league.getRank();
            System.out.println("league : " + league.getLeagueName() + " " + league.getRank() + " " + league.getGroups().size());
            long waitUser = 0;
            long degradeUser = 0;
            long advanceUser = 0;
            LeagueSummary currentSummary = leagueSummaryRepository.findByLeagueId(league.getId()).get();
            Optional<League> advanceLeague = leagueRepository.findFirstByRankGreaterThanOrderByRankAsc(currentRank);
            Optional<League> degradeLeague = leagueRepository.findFirstByRankLessThanOrderByRankDesc(currentRank);
            if (degradeLeague.isPresent()) {
                advanceUser += leagueSummaryRepository.findByLeagueId(degradeLeague.get().getId()).get().getAdvancedUsers().size();
            }
            if (advanceLeague.isPresent()) {
                degradeUser += leagueSummaryRepository.findByLeagueId(advanceLeague.get().getId()).get().getDeGradeUsers().size();
            }
            waitUser += currentSummary.getWaitUsers().size();
            Long actualUserSize = league.getGroups().stream()
                    .mapToLong(group -> group.getUsers().size())
                    .sum();
            long totalUserSize = advanceUser + waitUser + degradeUser;
            System.out.println("실제 사용자 : " + actualUserSize);
            System.out.println("승급 : " + advanceUser + " 강등 :" + degradeUser + " 대기: " + waitUser);
            assertEquals("Promoted users count mismatch", actualUserSize, totalUserSize);
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("소요 시간 " + duration + " ms.");
    }
}
