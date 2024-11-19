package com.ssafy.runit.domain.rank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DistanceRankManager {
    private final DistanceRankingService distanceRankingService;

    public Map<String, Integer> getRankDiff(long groupId) {
        return distanceRankingService.getRankDiff(groupId);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getGroupRanking(long groupId) {
        return distanceRankingService.getGroupRanking(groupId, 1);
    }

    public void updateDistance(long groupId, String userIdStr, double changed) {
        distanceRankingService.updateScore(groupId, userIdStr, changed);
    }

    public void deleteRank(String groupId) {
        distanceRankingService.removeSortedSetKey(groupId);
    }
}
