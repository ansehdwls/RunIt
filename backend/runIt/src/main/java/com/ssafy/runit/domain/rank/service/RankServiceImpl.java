package com.ssafy.runit.domain.rank.service;

import com.ssafy.runit.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankServiceImpl implements RankService {

    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public Map<String, Integer> getRankDiff(List<String> ids, long groupId) {
        String previousRankKey = String.format("previous_rank:%s", groupId);
        return redisTemplate.opsForHash().entries(previousRankKey)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> Integer.parseInt(entry.getValue().toString())
                ));
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> getGroupRanking(long groupId) {
        String key = String.format("ranking:%s", groupId);
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);
    }

    @Override
    public void updateScore(long groupId, String userIdStr, long changed) {
        log.debug("[updateScore] groupId: {}, userId: {}, changed: {}", groupId, userIdStr, changed);
        String rankingKey = String.format("ranking:%s", groupId);
        String previousRankKey = String.format("previous_rank:%s", groupId);
        Long previousRank = getPreviousRank(rankingKey, userIdStr);
        double timeAdjustment = calculateTimeAdjustment();
        updateUserScore(rankingKey, userIdStr, changed, timeAdjustment);
        Long newRank = redisTemplate.opsForZSet().reverseRank(rankingKey, userIdStr);
        if (newRank == null) {
            return;
        }
        int rankChange = previousRank.intValue() - newRank.intValue();
        if (rankChange == 0) {
            redisTemplate.delete(previousRankKey);
            return;
        }
        List<String> affectedUserIds = getAffectedUserIds(rankingKey, previousRank, newRank, rankChange, userIdStr);
        Map<String, Integer> rankChangesMap = buildRankChangesMap(userIdStr, rankChange, affectedUserIds);
        rankChangesMap.put(userIdStr, rankChange);
        updatePreviousRank(previousRankKey, rankChangesMap);
    }

    private void updateUserScore(String rankingKey, String userIdStr, long changed, double timeAdjustment) {
        double increment = changed + timeAdjustment;
        redisTemplate.opsForZSet().incrementScore(rankingKey, userIdStr, increment);
        redisTemplate.expire(rankingKey, DateUtils.computeDurationForNextWeek());
    }

    private Long getPreviousRank(String rankingKey, String userIdStr) {
        Long previousRank = redisTemplate.opsForZSet().reverseRank(rankingKey, userIdStr);
        if (previousRank == null) {
            previousRank = redisTemplate.opsForZSet().zCard(rankingKey);
        }
        return previousRank;
    }


    private List<String> getAffectedUserIds(String rankingKey, Long previousRank, Long newRank, int rankChange, String userIdStr) {
        List<String> affectedUserIds = new ArrayList<>();
        Set<Object> affectedUsers;
        if (rankChange > 0) {
            affectedUsers = redisTemplate.opsForZSet().reverseRange(rankingKey, newRank + 1, previousRank);
        } else {
            affectedUsers = redisTemplate.opsForZSet().reverseRange(rankingKey, previousRank + 1, newRank);
        }
        if (affectedUsers != null) {
            affectedUserIds.addAll(affectedUsers.stream().map(Object::toString).toList());
        }
        return affectedUserIds;
    }

    private Map<String, Integer> buildRankChangesMap(String userIdStr, int rankChange, List<String> affectedUserIds) {
        Map<String, Integer> rankChangesMap = new HashMap<>();
        for (String affectedUserId : affectedUserIds) {
            if (rankChange > 0) {
                rankChangesMap.put(affectedUserId, -1);
            } else {
                rankChangesMap.put(affectedUserId, 1);
            }
        }
        return rankChangesMap;
    }


    private void updatePreviousRank(String previousRankKey, Map<String, Integer> rankChangesMap) {
        redisTemplate.delete(previousRankKey);
        redisTemplate.opsForHash().putAll(previousRankKey, rankChangesMap);
        redisTemplate.expire(previousRankKey, DateUtils.computeDurationForNextWeek());
    }

    private double calculateTimeAdjustment() {
        long currentTimeMillis = System.currentTimeMillis();
        return (currentTimeMillis % 1000000) * 1e-9; // 0 ~ 0.000999999 초 단위
    }
}
