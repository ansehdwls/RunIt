package com.ssafy.runit.domain.rank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExperienceRankManager {

    private final ExperiencePointRankingService experiencePointRankingService;

    public Map<String, Integer> getRankDiff(long groupId) {
        return experiencePointRankingService.getRankDiff(groupId);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getGroupRanking(long groupId) {
        return experiencePointRankingService.getGroupRanking(groupId, 1);
    }

    public void updateScore(long groupId, String userIdStr, long changed) {
        experiencePointRankingService.updateScore(groupId, userIdStr, changed);
    }

    public void deleteRank(String groupId) {
        experiencePointRankingService.removeSortedSetKey(groupId);
    }
}
