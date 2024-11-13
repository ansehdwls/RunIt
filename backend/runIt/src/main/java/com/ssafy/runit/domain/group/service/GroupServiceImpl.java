package com.ssafy.runit.domain.group.service;

import com.ssafy.runit.domain.group.dto.response.GetGroupUsersInfo;
import com.ssafy.runit.domain.group.dto.response.GroupUserInfo;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.rank.RankType;
import com.ssafy.runit.domain.rank.service.ExperienceRankManager;
import com.ssafy.runit.domain.rank.service.PaceRankManager;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.GroupErrorCode;
import com.ssafy.runit.exception.code.ServerErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final ExperienceRankManager experienceRankManager;
    private final PaceRankManager paceRankManager;

    @Override
    @Transactional(readOnly = true)
    public GetGroupUsersInfo findUsersByGroupWithRank(Long groupId, String userNumber, String rankType) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND_ERROR)
        );
        User user = group.getUsers().stream()
                .filter(u -> u.getUserNumber().equals(userNumber))
                .findFirst()
                .orElseThrow(() -> new CustomException(GroupErrorCode.GROUP_NO_USERS_ERROR));
        Map<String, User> userMap = group.getUsers().stream()
                .collect(Collectors.toMap(u -> String.valueOf(u.getId()), u -> u));
        RankType type = RankType.fromString(rankType);
        switch (type) {
            case EXPERIENCE -> {
                return GetExperienceRank(group, user, userMap);
            }
            case PACE -> {
                return GetPaceRank(group, user, userMap);
            }
            default -> throw new CustomException(ServerErrorCode.UNKNOWN_SERVER_ERROR);
        }
    }

    public GetGroupUsersInfo GetPaceRank(Group group, User user, Map<String, User> userMap) {
        Map<String, Integer> rankDiff = paceRankManager.getRankDiff(group.getId()); // 랭크 변화
        Set<ZSetOperations.TypedTuple<Object>> rankings = paceRankManager.getGroupRanking(group.getId());
        List<GroupUserInfo> userInfos = rankings.stream()
                .map(ranking -> {
                    String userId = String.valueOf(ranking.getValue());
                    Double score = ranking.getScore();
                    score = Math.round(score * 100) / 100.0;
                    int diff = rankDiff.getOrDefault(userId, 0);
                    User u = userMap.get(userId);
                    if (u == null) {
                        return null;
                    }
                    return GroupUserInfo.fromEntity(u, getPaceFormatted(score), diff);
                })
                .toList();
        return new GetGroupUsersInfo().Mapper(userInfos, user, group.getGroupLeague().getRank().getRank());
    }

    public GetGroupUsersInfo GetExperienceRank(Group group, User user, Map<String, User> userMap) {
        Map<String, Integer> rankDiff = experienceRankManager.getRankDiff(group.getId()); // 랭크 변화
        Set<ZSetOperations.TypedTuple<Object>> rankings = experienceRankManager.getGroupRanking(group.getId());
        List<GroupUserInfo> userInfos = rankings.stream()
                .map(ranking -> {
                    String userId = String.valueOf(ranking.getValue());
                    Double score = ranking.getScore();
                    score = Math.round(score * 100) / 100.0;
                    int diff = rankDiff.getOrDefault(userId, 0);
                    User u = userMap.get(userId);
                    if (u == null) {
                        return null;
                    }
                    return GroupUserInfo.fromEntity(u, String.valueOf(score), diff);
                })
                .toList();
        return new GetGroupUsersInfo().Mapper(userInfos, user, group.getGroupLeague().getRank().getRank());
    }

    public String getPaceFormatted(Double pace) {
        if (pace == null) {
            return "N/A";
        }
        int minutes = pace.intValue();
        int seconds = (int) Math.round((pace - minutes) * 60);
        return String.format("%d'%02d'/km", minutes, seconds);
    }
}
