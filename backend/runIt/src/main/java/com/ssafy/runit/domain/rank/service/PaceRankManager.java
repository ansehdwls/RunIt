package com.ssafy.runit.domain.rank.service;

import com.ssafy.runit.config.redis.RedisKeys;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.ServerErrorCode;
import com.ssafy.runit.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaceRankManager {

    private final AveragePaceRankingService averagePaceRankingService;
    private final RedisTemplate<String, Object> redisTemplate;


    public void savePace(Record record, String groupId, String userId) {
        double newPace = updateUserPace(record.getPace(), userId);
        averagePaceRankingService.updateScore(Long.parseLong(groupId), userId, newPace);
    }

    private double updateUserPace(double averagePace, String userId) {
        String key = averagePaceRankingService.getSubKey(userId);
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        hashOperations.increment(key, RedisKeys.USER_PACE_SUB_KEY_TOTAL_PACE, averagePace);
        hashOperations.increment(key, RedisKeys.USER_PACE_SUB_KEY_COUNT, 1);
        redisTemplate.expire(key, DateUtils.computeDurationForNextWeek());
        Double totalPace = (Double) hashOperations.get(key, RedisKeys.USER_PACE_SUB_KEY_TOTAL_PACE);
        Integer totalCount = (Integer) hashOperations.get(key, RedisKeys.USER_PACE_SUB_KEY_COUNT);

        if (totalCount == null || totalPace == null || totalCount == 0) {
            throw new CustomException(ServerErrorCode.REDIS_RETRIEVAL_ERROR);
        }
        double newPace = totalPace / totalCount;
        newPace = Math.round(newPace * 100.0) / 100.0;

        return newPace;
    }
}
