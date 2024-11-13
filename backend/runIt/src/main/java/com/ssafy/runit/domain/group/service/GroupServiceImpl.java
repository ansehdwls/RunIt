package com.ssafy.runit.domain.group.service;

import com.ssafy.runit.domain.group.dto.response.GetGroupUsersInfo;
import com.ssafy.runit.domain.group.dto.response.GroupUserInfo;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.rank.RankType;
import com.ssafy.runit.domain.rank.service.DistanceRankManager;
import com.ssafy.runit.domain.rank.service.ExperienceRankManager;
import com.ssafy.runit.domain.rank.service.PaceRankManager;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.GroupErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final ExperienceRankManager experienceRankManager;
    private final PaceRankManager paceRankManager;
    private final DistanceRankManager distanceRankManager;

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
                return GetExperienceRank(group, user, userMap, RankType.EXPERIENCE);
            }
            case PACE -> {
                return GetPaceRank(group, user, userMap, RankType.PACE);
            }

            case DISTANCE -> {
                return GetDistanceRank(group, user, userMap, RankType.DISTANCE);
            }
            default -> throw new CustomException(GroupErrorCode.INVALID_RANK_TYPE_ERROR);
        }
    }

    public GetGroupUsersInfo GetPaceRank(Group group, User user, Map<String, User> userMap, RankType type) {
        return getGetGroupUsersInfo(group, user, userMap, paceRankManager.getRankDiff(group.getId()), paceRankManager.getGroupRanking(group.getId()), type);
    }

    public GetGroupUsersInfo GetExperienceRank(Group group, User user, Map<String, User> userMap, RankType type) {
        return getGetGroupUsersInfo(group, user, userMap, experienceRankManager.getRankDiff(group.getId()), experienceRankManager.getGroupRanking(group.getId()), type);
    }

    public GetGroupUsersInfo GetDistanceRank(Group group, User user, Map<String, User> userMap, RankType type) {
        return getGetGroupUsersInfo(group, user, userMap, distanceRankManager.getRankDiff(group.getId()), distanceRankManager.getGroupRanking(group.getId()), type);
    }

    private GetGroupUsersInfo getGetGroupUsersInfo(Group group, User user, Map<String, User> userMap, Map<String, Integer> rankDiff2, Set<ZSetOperations.TypedTuple<Object>> groupRanking, RankType type) {
        List<GroupUserInfo>  userInfos = groupRanking.stream()
                    .map(ranking -> {
                        String userId = String.valueOf(ranking.getValue());
                        Double score = Objects.requireNonNullElse(ranking.getScore(), 0.0);
                        int diff = rankDiff2.getOrDefault(userId, 0);
                        User u = userMap.get(userId);
                        userMap.remove(userId);
                        if (u == null) {
                            return null;
                        }
                        return GroupUserInfo.fromEntity(u, RankType.calRankScore(type, score), diff);
                    })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        userInfos.addAll(userMap.values().stream()
                .map(u -> GroupUserInfo.fromEntity(u, RankType.calRankScore(type, 0.0), 0)) // 기본값 적용
                .toList());
        return new GetGroupUsersInfo().Mapper(userInfos, user, group.getGroupLeague().getRank().getRank());
    }
}
