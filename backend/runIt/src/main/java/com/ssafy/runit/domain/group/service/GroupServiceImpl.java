package com.ssafy.runit.domain.group.service;

import com.ssafy.runit.domain.group.dto.response.GetGroupUsersInfo;
import com.ssafy.runit.domain.group.dto.response.GroupUserInfo;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.rank.service.RankService;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.GroupErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final RankService rankService;

    @Override
    @Transactional(readOnly = true)
    public GetGroupUsersInfo findUsersByGroupWithRank(Long groupId, String userNumber) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND_ERROR)
        );
        User user = group.getUsers().stream()
                .filter(u -> u.getUserNumber().equals(userNumber))
                .findFirst()
                .orElseThrow(() -> new CustomException(GroupErrorCode.GROUP_NO_USERS_ERROR));
        Map<String, User> userMap = group.getUsers().stream()
                .collect(Collectors.toMap(u -> String.valueOf(u.getId()), u -> u));
        List<String> userIds = new ArrayList<>(userMap.keySet());
        Map<String, Integer> rankDiff = rankService.getRankDiff(userIds, groupId); // 랭크 변화
        Set<ZSetOperations.TypedTuple<Object>> rankings = rankService.getGroupRanking(groupId);
        List<GroupUserInfo> userInfos = rankings.stream()
                .map(ranking -> {
                    String userId = String.valueOf(ranking.getValue());
                    Double score = ranking.getScore();
                    int diff = rankDiff.getOrDefault(userId, 0);
                    User u = userMap.get(userId);
                    if (u == null) {
                        return null;
                    }
                    return GroupUserInfo.fromEntity(u, score.longValue(), diff);
                })
                .toList();
        return new GetGroupUsersInfo().Mapper(userInfos, user, group.getGroupLeague().getRank().getRank());
    }
}
