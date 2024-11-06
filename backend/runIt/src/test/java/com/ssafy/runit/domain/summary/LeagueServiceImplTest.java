package com.ssafy.runit.domain.summary;

import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.league.entity.League;
import com.ssafy.runit.domain.league.repository.LeagueRepository;
import com.ssafy.runit.domain.rank.LeagueRank;
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
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
public class LeagueServiceImplTest {

    @Autowired
    private SummaryFactory summaryFactory;
    @Autowired
    private LeagueSummaryService leagueSummaryService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private LeagueSummaryRepository leagueSummaryRepository;


    @BeforeEach
    @Transactional
    public void before() {
        summaryFactory.createTestData();
        System.out.println("Test Start");
    }


    @AfterEach
    public void after() {
        System.out.println("Test End");
    }

    @Test
    @DisplayName("주간 리그 결산 테스트")
    @Transactional // 테스트 시 트랜잭션 롤백을 원할 경우 사용
    public void weeklyLeagueSummaryTest() {
        // 1. 테스트 데이터 생성
        int groupsPerLeague = 13;
        int usersPerGroup = 10;
        // 2. 성능 측정 시작
        long startTime = System.currentTimeMillis();
        System.out.println("processWeeklySummary Start");
        summaryFactory.createLeagues(groupsPerLeague, usersPerGroup);
        // 3. 주간 요약 처리 메서드 호출
        leagueSummaryService.processWeeklySummary();
        // 4. 리그 인원 수 검사
        List<League> leagues = leagueRepository.findAll();
        long totalUserSize = leagues.stream()
                .mapToLong(league -> league.getGroups().stream()
                        .mapToLong(group -> group.getUsers().size())
                        .sum())
                .sum();
        long testUserSize = 0;
        for (League league : leagues) {
            LeagueRank currentRank = league.getRank();
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
            long currentUserSize = advanceUser + waitUser + degradeUser;
            System.out.println("[실제] 사용자 수 : " + actualUserSize);
            System.out.println("[예측] 사용자 수 : " + currentUserSize + " 승급 : " + advanceUser + " 강등 :" + degradeUser + " 대기: " + waitUser);
            //assertEquals("Promoted users count mismatch", actualUserSize, currentUserSize);
            testUserSize += currentUserSize;
        }
        System.out.println("[실제] 전체 유저 수 : " + totalUserSize);
        System.out.println("[예측] 전체 유저 수 :" + testUserSize);
        assertEquals("User Count mismatch", totalUserSize, testUserSize);
        // 5. 성능 측정
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("소요 시간 " + duration + " ms.");
    }

    @Test
    @DisplayName("그룹이 부족할 경우 그룹을 새로 할당하고 배정한다.")
    @Transactional
    public void GroupAddTest() {
        // 1. 테스트 데이터 생성
        int testUserSize = 31;
        // 2. 성능 측정
        long startTime = System.currentTimeMillis();
        System.out.println("GroupAddTest Start");
        summaryFactory.crateAddTestData(testUserSize);
        // 3. 주간 결산 메서드 실행
        leagueSummaryService.processWeeklySummary();
        // 4. 다음 리그에 그룹 수와 배정받은 사용자의 수를 검
        League league = leagueRepository.findLeagueWithGroups(LeagueRank.fromRank(2));
        List<Group> groups = league.getGroups().stream().sorted((g1, g2) -> Integer.compare(g2.getUsers().size(), g1.getUsers().size())).toList();
        int needGroupSize = (int) Math.ceil((double) testUserSize / 10); //필요한 그룹 개수
        int averageUserCount = testUserSize / needGroupSize;
        long actualUserSize = groups.stream().mapToLong(group -> group.getUsers().size()).sum();
        System.out.println("총 인원수 : " + actualUserSize + " 그룹수 : " + groups.size());
        System.out.println("필요한 그룹 : " + needGroupSize + " 인원배정 :" + averageUserCount);
        int res = testUserSize % needGroupSize;
        System.out.println(" group size [예상] : " + needGroupSize + " [실제] : " + league.getGroups().size());
        assertEquals("group size is not match", needGroupSize, league.getGroups().size());
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            int userAssignCount = averageUserCount + (i < res ? 1 : 0);
            System.out.println("카운팅 " + i + " " + userAssignCount);
            System.out.println("user size [예상] " + userAssignCount + " [실제] " + group.getUsers().size());
            assertEquals("assign user is not equals", userAssignCount, group.getUsers().size());
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("소요 시간 " + duration + " ms.");
    }
}
