package com.ssafy.runit.domain.group.service;

import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.experience.repository.ExperienceRepository;
import com.ssafy.runit.domain.group.dto.response.GetGroupUsersResponse;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.league.entity.League;
import com.ssafy.runit.domain.league.repository.LeagueRepository;
import com.ssafy.runit.domain.rank.LeagueRank;
import com.ssafy.runit.domain.summary.SummaryFactory;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
public class GroupServiceImplTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private TestDataUtil testDataUtil;

    @Autowired
    private SummaryFactory summaryFactory;
    @Autowired
    private LeagueRepository leagueRepository;


    @BeforeEach
    public void setUp() {
        summaryFactory.createTestData();
    }

    @Test
    @DisplayName("그룹 별 사용자 경험치 합계 및 랭킹 조회 테스트")
    @Transactional
    public void testFindUsersByGroupPerformance() {
        int groupCount = 10;
        int userPerGroup = 10;
        int experiencePerUser = 1000;
        League league = leagueRepository.findFirstByRankGreaterThanOrderByRankAsc(LeagueRank.RANK_1).get();
        List<User> users = new ArrayList<>();
        List<Experience> experiences = new ArrayList<>();
        List<Group> groups = testDataUtil.createTestGroups(league, groupCount);
        groupRepository.saveAll(groups);
        for (Group group : groups) {
            users.addAll(testDataUtil.createTestUsers(group, userPerGroup));
        }
        userRepository.saveAll(users);
        for (User user : users) {
            experienceRepository.saveAll(testDataUtil.createTestExperiences(user, experiencePerUser));
        }
        experienceRepository.saveAll(experiences);
        long startTime = System.currentTimeMillis();
        for (Group group : groups) {
            List<GetGroupUsersResponse> responses = groupService.findUsersByGroup(group.getId());
            for (int i = 0; i < responses.size() - 1; i++) {
                long current = responses.get(i).getExp();
                long next = responses.get(i + 1).getExp();
                assertTrue("", current >= next);
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("total time is " + duration + "ms");
    }
}
