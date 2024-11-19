package com.ssafy.runit.domain.rank.service;

import com.ssafy.runit.config.redis.RedisKeys;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.ServerErrorCode;
import com.ssafy.runit.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaceRankManager {

    private final AveragePaceRankingService averagePaceRankingService;
    private final RedisTemplate<String, Object> redisTemplate;

    public void updatePace(Record record, String groupId, String userId) {
        double newPace = updateUserPace(record.getPace(), userId);
        averagePaceRankingService.updateScore(Long.parseLong(groupId), userId, newPace);
    }

    private double updateUserPace(double averagePace, String userId) {
        String key = averagePaceRankingService.getSubKey(userId);
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        hashOperations.increment(key, RedisKeys.USER_PACE_SUB_KEY_TOTAL_PACE, averagePace);
        hashOperations.increment(key, RedisKeys.USER_PACE_SUB_KEY_COUNT, 1);
        redisTemplate.expire(key, DateUtils.computeDurationForNextWeek());
        Number totalPace = (Number) hashOperations.get(key, RedisKeys.USER_PACE_SUB_KEY_TOTAL_PACE);
        Number totalCount = (Number) hashOperations.get(key, RedisKeys.USER_PACE_SUB_KEY_COUNT);

        if (totalCount == null || totalPace == null || totalCount.intValue() == 0) {
            throw new CustomException(ServerErrorCode.REDIS_RETRIEVAL_ERROR);
        }
        double newPace = totalPace.doubleValue() / totalCount.intValue();
        newPace = Math.round(newPace * 100.0) / 100.0;

        return newPace;
    }

    public Map<String, Integer> getRankDiff(long groupId) {
        return averagePaceRankingService.getRankDiff(groupId);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getGroupRanking(long groupId) {
        return averagePaceRankingService.getGroupRanking(groupId, -1);
    }

    public void deleteRank(String groupId) {
        averagePaceRankingService.removeSortedSetKey(groupId);
    }

    public void deleteHash(String userId) {
        String key = averagePaceRankingService.getSubKey(userId);
        String [] fields = {"total_pace", "count"};
        try {
            Long result = redisTemplate.opsForHash().delete(key, fields);
            log.info("HDEL 수행 완료. 키: {}, 삭제된 필드 수: {}", key, result);
        } catch (Exception e) {
            log.error("HDEL 수행 중 오류 발생. 키: {}, 오류: {}", key, e.getMessage(), e);
        }
    }
}
