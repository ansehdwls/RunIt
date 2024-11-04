package com.ssafy.runit.summary;

import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.experience.repository.ExperienceRepository;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.league.entity.League;
import com.ssafy.runit.domain.league.repository.LeagueRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
public class SummaryFactory {

    private final LeagueRepository leagueRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ExperienceRepository experienceRepository;

    public SummaryFactory(LeagueRepository leagueRepository, GroupRepository groupRepository, UserRepository userRepository, ExperienceRepository experienceRepository) {
        this.leagueRepository = leagueRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.experienceRepository = experienceRepository;
    }

    public void createTestData() {
        String[] list = {"알", "나무늘보", "거북이", "토끼", "말", "치타"};
        for (int i = 0; i < 6; i++) {
            League league = League.builder()
                    .leagueName(list[i])
                    .groups(new HashSet<>())
                    .rank(i + 1).build();
            leagueRepository.save(league);
            if (i == 0) {
                groupRepository
                        .save(Group.builder().groupLeague(league).users(new HashSet<>()).build());
            }
        }
    }

    public void createLeagues(int groupsPerLeague, int usersPerGroup) {
        List<League> leagues = leagueRepository.findAllByOrderByRankAsc();

        for (League league : leagues) {
            Set<Group> groups = LongStream.rangeClosed(1, groupsPerLeague)
                    .mapToObj(groupNum -> Group.builder()
                            .groupLeague(league)
                            .users(new HashSet<>())
                            .build())
                    .collect(Collectors.toSet());

            groupRepository.saveAll(groups);
            league.updateGroups(groups);
            for (Group group : groups) {
                Set<User> users = LongStream.rangeClosed(1, usersPerGroup)
                        .mapToObj(userNum -> {
                            User user = User.builder()
                                    .userNumber("User_" + userNum)
                                    .userName("User_" + group.getId() + "_" + userNum)
                                    .imageUrl("test")
                                    .experiences(new HashSet<>())
                                    .fcmToken("test")
                                    .build();
                            user.updateGroup(group);
                            return user;
                        })
                        .collect(Collectors.toSet());
                userRepository.saveAll(users);
                for (User user : users) {
                    Experience experience = Experience.builder()
                            .changed(assignExperience(user.getId()))
                            .createAt(LocalDateTime.now())
                            .user(user)
                            .build();
                    experienceRepository.save(experience);
                    user.addExperience(experience);
                }
            }
        }
    }

    @Transactional
    public void crateAddTestData(int testUserSize) {
        League testLeague = leagueRepository.findAllByOrderByRankAsc().stream().toList().get(0);
        System.out.println("league : " + testLeague.getId() + " " + testLeague.getLeagueName() + " " + testLeague.getRank());
        Group testGroup = Group.builder()
                .groupLeague(testLeague)
                .users(new HashSet<>())
                .build();
        groupRepository.save(testGroup);
        testLeague.addGroup(testGroup);
        for (int i = 0; i < testUserSize; i++) {
            User user = User.builder()
                    .userName("test-user" + i)
                    .userNumber("test-num" + i)
                    .experiences(new HashSet<>())
                    .fcmToken("test-token" + i)
                    .imageUrl("test-imageUrl" + i)
                    .build();
            user.updateGroup(testGroup);
            userRepository.save(user);
            Experience experience = Experience.builder()
                    .changed(assignExperience(user.getId()))
                    .createAt(LocalDateTime.now())
                    .user(user)
                    .build();
            user.addExperience(experience);
            experienceRepository.save(experience);
        }
    }

    public Long assignExperience(long userNum) {
        if (userNum % 5 == 0) {
            return 2000L * userNum;
        } else if (userNum % 2 == 0) {
            return 1000L * userNum;
        } else {
            return 500L * userNum;
        }
    }
}
