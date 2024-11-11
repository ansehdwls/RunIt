package com.ssafy.runit.domain.rank.service;

import com.ssafy.runit.domain.user.dto.response.UserInfoResponse;
import com.ssafy.runit.domain.user.service.UserService;
import com.ssafy.runit.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankServiceImpl implements RankService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;

    @Override
    public void updateScore(long groupId, long userId, long changed) {
        log.debug("[updateScore] groupId:{} userId:{} changed: {}", groupId, userId, changed);
        String key = String.format("ranking:%s", groupId);
        String previousRankKey = String.format("previous_rank:%s", groupId);
        List<UserInfoResponse> users = userService.findUserByGroup(groupId);
        List<String> ids = users.stream().map(UserInfoResponse::getUserId).map(String::valueOf).collect(Collectors.toList());
        Map<String, Integer> previousRank = getCurrentRanks(ids, groupId);
        long currentTimeMillis = System.currentTimeMillis();
        double timeAdjustment = (currentTimeMillis % 1000000) * 1e-6;
        redisTemplate.opsForHash().putAll(previousRankKey, previousRank);
        redisTemplate.expire(previousRankKey, DateUtils.computeDurationForNextWeek());
        redisTemplate.opsForZSet().incrementScore(key, String.valueOf(userId), changed + timeAdjustment);
        redisTemplate.expire(key, DateUtils.computeDurationForNextWeek());
        redisTemplate.expire(previousRankKey, DateUtils.computeDurationForNextWeek());
    }


    @Override
    public Map<String, Integer> getPreviousRanks(List<String> userIds, long groupId) {
        String previousRankKey = String.format("previous_rank:%s", groupId);
        Map<String, Integer> previousRanks = new HashMap<>();
        List<Object> ids = userIds.stream().map(String::valueOf).collect(Collectors.toList());
        List<Object> rankStrings = redisTemplate.opsForHash().multiGet(previousRankKey, ids);
        for (int i = 0; i < userIds.size(); i++) {
            Object rankObj = rankStrings.get(i);
            Integer rank = rankObj != null ? Integer.parseInt(rankObj.toString()) : null;
            previousRanks.put(userIds.get(i), rank);
        }
        return previousRanks;
    }

    @Override
    public Map<String, Integer> getCurrentRanks(List<String> ids, long groupId) {
        String key = String.format("ranking:%s", groupId);
        Map<String, Integer> currentRanks = new HashMap<>();
        for (String userId : ids) {
            Long rank = redisTemplate.opsForZSet().reverseRank(key, String.valueOf(userId));
            if (rank != null) {
                currentRanks.put(userId, (int) (rank + 1));
            }
        }
        return currentRanks;
    }

    @Override
    public Map<String, Integer> getRankDiff(List<String> ids, long groupId) {
        Map<String, Integer> previousRanks = getPreviousRanks(ids, groupId);
        Map<String, Integer> currentRanks = getCurrentRanks(ids, groupId);
        Map<String, Integer> rankChanges = new HashMap<>();
        for (String uid : ids) {
            Integer previousRank = previousRanks.get(uid);
            Integer currentRank = currentRanks.get(uid);
            if (previousRank == null) {
                previousRank = currentRank;
            }
            int rankChange = previousRank - currentRank;
            rankChanges.put(uid, rankChange);
        }
        return rankChanges;
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> getGroupRanking(long groupId) {
        String key = String.format("ranking:%s", groupId);
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);
    }
}
