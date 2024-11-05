package com.ssafy.runit.domain.summary.service;

import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.league.entity.League;
import com.ssafy.runit.domain.rank.LeagueRank;
import com.ssafy.runit.domain.league.repository.LeagueRepository;
import com.ssafy.runit.domain.summary.entity.LeagueSummary;
import com.ssafy.runit.domain.summary.repository.LeagueSummaryRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeagueSummaryServiceImpl implements LeagueSummaryService {

    private final LeagueRepository leagueRepository;
    private final LeagueSummaryRepository leagueSummaryRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public void processWeeklySummary() {
        LocalDate lastMonday = DateUtils.getLastMonday().minusDays(7);
        List<League> leagues = leagueRepository.findAllByOrderByRankAsc();

        Map<Long, Set<User>> advanceMap = new HashMap<>();
        Map<Long, Set<User>> degradeMap = new HashMap<>();
        Map<Long, Set<User>> waitMap = new HashMap<>();

        for (League league : leagues) {
            processLeague(league, lastMonday, advanceMap, degradeMap, waitMap);
        }

        assignUsersToNewGroups(leagues, advanceMap, degradeMap, waitMap);
    }

    private void processLeague(League league, LocalDate lastMonday,
                               Map<Long, Set<User>> advanceMap,
                               Map<Long, Set<User>> degradeMap,
                               Map<Long, Set<User>> waitMap) {
        log.debug("[league]-{} - 할당 시작!", league.getLeagueName());
        Set<Group> groups = league.getGroups();

        Set<User> advanceUser = new HashSet<>();
        Set<User> degradeUser = new HashSet<>();
        Set<User> waitUser = new HashSet<>();

        for (Group group : groups) {
            processGroup(group, lastMonday, league, advanceUser, degradeUser, waitUser);
        }

        advanceMap.put(league.getId(), advanceUser);
        degradeMap.put(league.getId(), degradeUser);
        waitMap.put(league.getId(), waitUser);

        LeagueSummary summary = LeagueSummary.builder()
                .league(league)
                .advancedUsers(advanceUser)
                .deGradeUsers(degradeUser)
                .waitUsers(waitUser)
                .build();
        leagueSummaryRepository.save(summary);
    }

    private void processGroup(Group group, LocalDate lastMonday, League league,
                              Set<User> advanceUser, Set<User> degradeUser, Set<User> waitUser) {
        List<User> sortedUsers = userRepository.findUsersWithExperienceSum(group.getId(), lastMonday.atStartOfDay());
        if (sortedUsers.isEmpty()) {
            return;
        }

        int[] counts = league.getRank().getLeagueRankService().calculateAdvanceDegradeCounts(sortedUsers.size());
        int advanceCount = counts[0];
        int degradeCount = counts[1];
        int waitCount = counts[2];
        System.out.println("승급 : " + advanceCount + " 강등 : " + degradeCount + " 대기:" + waitCount);
        allocateUsersToCategories(sortedUsers, advanceCount, degradeCount, advanceUser, degradeUser, waitUser);
    }

    private void allocateUsersToCategories(List<User> sortedUsers, int advanceCount, int degradeCount,
                                           Set<User> advanceUser, Set<User> degradeUser, Set<User> waitUser) {
        if (advanceCount > 0) {
            advanceUser.addAll(sortedUsers.subList(0, Math.min(advanceCount, sortedUsers.size())));
        }
        if (degradeCount > 0) {
            int degradeStartIndex = Math.max(sortedUsers.size() - degradeCount, advanceCount);
            degradeUser.addAll(sortedUsers.subList(degradeStartIndex, sortedUsers.size()));
        }

        if (advanceCount > 0 || degradeCount > 0) {
            waitUser.addAll(sortedUsers.subList(advanceCount, sortedUsers.size() - degradeCount));
        } else {
            waitUser.addAll(sortedUsers);
        }
    }

    @Transactional
    public void assignUsersToNewGroups(List<League> leagues,
                                       Map<Long, Set<User>> advanceMap,
                                       Map<Long, Set<User>> degradeMap,
                                       Map<Long, Set<User>> waitMap) {
        for (League league : leagues) {
            log.debug("{} 인원 할당", league.getLeagueName());
            List<User> totalUsers = gatherUsersForLeague(league, advanceMap, degradeMap, waitMap);
            if (totalUsers.isEmpty()) {
                log.debug("할당된 인원 없음!");
                continue;
            }
            assignUsersToGroups(totalUsers, league);
        }
    }


    private List<User> gatherUsersForLeague(League league,
                                            Map<Long, Set<User>> advanceMap,
                                            Map<Long, Set<User>> degradeMap,
                                            Map<Long, Set<User>> waitMap) {
        long currentLeagueId = league.getId();
        LeagueRank currentRank = league.getRank();

        Optional<League> higherLeague = leagueRepository.findFirstByRankGreaterThanOrderByRankAsc(currentRank);
        Optional<League> lowerLeague = leagueRepository.findFirstByRankLessThanOrderByRankDesc(currentRank);

        List<User> totalUsers = new ArrayList<>();

        lowerLeague.ifPresent(l -> totalUsers.addAll(advanceMap.getOrDefault(l.getId(), Collections.emptySet())));
        long advanceCount = totalUsers.size();

        higherLeague.ifPresent(l -> totalUsers.addAll(degradeMap.getOrDefault(l.getId(), Collections.emptySet())));
        long degradeCount = totalUsers.size() - advanceCount;

        totalUsers.addAll(waitMap.getOrDefault(currentLeagueId, Collections.emptySet()));
        long waitCount = totalUsers.size() - degradeCount - advanceCount;

        log.debug("승격 : {} 강등 : {} 대기 : {}", advanceCount, degradeCount, waitCount);
        return totalUsers;
    }

    @Transactional
    public void assignUsersToGroups(List<User> users, League league) {
        int maxGroupSize = 10;
        List<Group> groups = groupRepository.findAllByGroupLeague(league);
        int needGroupSize = (int) Math.ceil((double) users.size() / maxGroupSize);
        int additionalGroups = needGroupSize - groups.size();

        createAdditionalGroups(additionalGroups, league);

        groups = groupRepository.findAllByGroupLeague(league);
        log.debug("최종 그룹 수:{}", groups.size());

        distributeUsersToGroups(users, groups, needGroupSize);
        league.updateGroups(new HashSet<>(groups));
    }

    private void createAdditionalGroups(int additionalGroups, League league) {
        while (additionalGroups > 0) {
            additionalGroups--;
            groupRepository.save(Group.builder()
                    .groupLeague(league)
                    .users(new HashSet<>())
                    .build());
        }
    }


    @Transactional
    public void distributeUsersToGroups(List<User> users, List<Group> groups, int needGroupSize) {
        int averageUserCount = users.size() / needGroupSize;
        int res = users.size() % needGroupSize;
        int totalIdx = 0;

        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            int userAssignCount = averageUserCount + (i < res ? 1 : 0);
            for (int j = 0; j < userAssignCount && totalIdx < users.size(); j++) {
                User assignUser = users.get(totalIdx++);
                assignUser.updateGroup(group);
            }
        }
    }
}
