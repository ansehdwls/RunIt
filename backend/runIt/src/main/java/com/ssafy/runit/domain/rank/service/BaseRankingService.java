package com.ssafy.runit.domain.rank.service;

import com.ssafy.runit.domain.rank.UpdateType;
import com.ssafy.runit.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public abstract class BaseRankingService<T> {

    protected final RedisTemplate<String, Object> redisTemplate;

    protected abstract String getRankKey(String groupId);

    protected abstract String getPreviousRankKey(String groupId);

    protected abstract double calculateScore(T data);

    protected UpdateType getUpdateType() {
        return UpdateType.ADD;
    }

    protected abstract boolean isDescendingOrder();


    public Set<ZSetOperations.TypedTuple<Object>> getGroupRanking(long groupId, int sortOpt) {
        String key = getRankKey(String.valueOf(groupId));
        if (sortOpt == 1) {
            return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);
        } else {
            return redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        }
    }

    public Map<String, Integer> getRankDiff(long groupId) {
        String previousRankKey = getPreviousRankKey(String.valueOf(groupId));
        return redisTemplate.opsForHash().entries(previousRankKey)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> Integer.parseInt(entry.getValue().toString())
                ));
    }

    public void updateScore(long groupId, String userIdStr, T data) {
        String rankKey = getRankKey(String.valueOf(groupId));
        String previousRankKey = getPreviousRankKey(String.valueOf(groupId));

        Long previousRank = getPreviousRank(rankKey, userIdStr);
        double newScore = calculateScore(data) + calculateTimeAdjustment();

        updateUserScore(rankKey, userIdStr, newScore, getUpdateType());
        Long newRank;
        if (isDescendingOrder()) {
            newRank = redisTemplate.opsForZSet().reverseRank(rankKey, userIdStr);
        } else {
            newRank = redisTemplate.opsForZSet().rank(rankKey, userIdStr);
        }
        if (newRank == null) {
            return;
        }
        int rankChange = previousRank.intValue() - newRank.intValue();
        if (rankChange == 0) {
            redisTemplate.delete(previousRankKey);
            return;
        }
        List<String> affectedUserIds = getAffectedUserIds(rankKey, previousRank, newRank, rankChange);
        Map<String, Integer> rankChangesMap = buildRankChangesMap(rankChange, affectedUserIds);
        rankChangesMap.put(userIdStr, rankChange);
        updatePreviousRank(previousRankKey, rankChangesMap);
    }

    protected void updateUserScore(String rankKey, String userIdStr, double newScore, UpdateType updateType) {
        switch (updateType) {
            case ADD -> redisTemplate.opsForZSet().add(rankKey, userIdStr, newScore);
            case INCREMENT -> redisTemplate.opsForZSet().incrementScore(rankKey, userIdStr, newScore);
            default -> throw new IllegalArgumentException("Unsupported UpdateType: " + updateType);
        }
        redisTemplate.expire(rankKey, DateUtils.computeDurationForNextWeek());
    }

    protected Long getPreviousRank(String rankKey, String userIdStr) {
        Long previousRank;
        if (isDescendingOrder()) {
            previousRank = redisTemplate.opsForZSet().reverseRank(rankKey, userIdStr);
        } else {
            previousRank = redisTemplate.opsForZSet().rank(rankKey, userIdStr);
        }
        if (previousRank == null) {
            previousRank = redisTemplate.opsForZSet().zCard(rankKey);
        }
        return previousRank;
    }

    protected List<String> getAffectedUserIds(String rankKey, Long previousRank, Long newRank, int rankChange) {
        List<String> affectedUserIds = new ArrayList<>();
        Set<Object> affectedUsers;
        if (isDescendingOrder()) {
            if (rankChange > 0) {
                affectedUsers = redisTemplate.opsForZSet().reverseRange(rankKey, newRank + 1, previousRank);
            } else {
                affectedUsers = redisTemplate.opsForZSet().reverseRange(rankKey, previousRank + 1, newRank);
            }
        } else {
            if (rankChange > 0) {
                affectedUsers = redisTemplate.opsForZSet().range(rankKey, newRank + 1, previousRank);
            } else {
                affectedUsers = redisTemplate.opsForZSet().range(rankKey, previousRank + 1, newRank);
            }
        }
        if (affectedUsers != null) {
            affectedUserIds.addAll(affectedUsers.stream().map(Object::toString).toList());
        }
        return affectedUserIds;
    }

    protected Map<String, Integer> buildRankChangesMap(int rankChange, List<String> affectedUserIds) {
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

    protected void updatePreviousRank(String previousRankKey, Map<String, Integer> rankChangesMap) {
        redisTemplate.delete(previousRankKey);
        redisTemplate.opsForHash().putAll(previousRankKey, rankChangesMap);
        redisTemplate.expire(previousRankKey, DateUtils.computeDurationForNextWeek());
    }

    protected double calculateTimeAdjustment() {
        long currentTimeMillis = System.currentTimeMillis();
        return (currentTimeMillis % 1000000) * 1e-9;
    }

    protected void removeSortedSetKey(String id) {
        redisTemplate.delete(getRankKey(id));
        redisTemplate.delete(getPreviousRankKey(id));
    }
}
