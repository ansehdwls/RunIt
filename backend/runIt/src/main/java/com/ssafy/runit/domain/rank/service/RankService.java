package com.ssafy.runit.domain.rank.service;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RankService {

    void updateScore(long groupId, long userId, long changed);

    Map<String, Integer> getPreviousRanks(List<String> userIds, long groupId);

    Map<String, Integer> getCurrentRanks(List<String> ids, long groupId);

    Map<String, Integer> getRankDiff(List<String> ids, long groupId);

    Set<ZSetOperations.TypedTuple<Object>> getGroupRanking(long groupId);
}
