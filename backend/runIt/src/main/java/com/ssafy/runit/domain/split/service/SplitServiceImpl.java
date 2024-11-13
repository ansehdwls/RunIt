package com.ssafy.runit.domain.split.service;

import com.ssafy.runit.config.redis.RedisKeys;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.split.repository.SplitRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.ServerErrorCode;
import com.ssafy.runit.exception.code.SplitErrorCode;
import com.ssafy.runit.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SplitServiceImpl implements SplitService {

    private final SplitRepository splitRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    @Override
    public void saveAllSplit(Record record) {
        String groupId = String.valueOf(record.getUser().getUserGroup().getId());
        String userId = String.valueOf(record.getUser().getId());
        splitRepository.saveAll(record.getSplitList());

        savePace(record, groupId, userId);

        //TODO 거리 저장 로직 추가
    }

    private void savePace(Record record, String groupId, String userId) {
        String paceRankKey = RedisKeys.getPaceRankKey(groupId);
        String paceHashKey = RedisKeys.getUserPaceKey(groupId);
        Double averagePace = record.getPace();

        if (averagePace == null) {
            throw new CustomException(SplitErrorCode.PACE_CALCULATION_ERROR);
        }

        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        hashOperations.increment(paceHashKey, RedisKeys.USER_PACE_SUB_KEY_TOTAL_PACE, averagePace);
        hashOperations.increment(paceHashKey, RedisKeys.USER_PACE_SUB_KEY_COUNT, 1);

        Double totalPace = (Double) hashOperations.get(paceHashKey, RedisKeys.USER_PACE_SUB_KEY_TOTAL_PACE);
        Integer totalCount = (Integer) hashOperations.get(paceHashKey, RedisKeys.USER_PACE_SUB_KEY_COUNT);

        if (totalCount == null || totalPace == null || totalCount == 0) {
            throw new CustomException(ServerErrorCode.REDIS_RETRIEVAL_ERROR);
        }

        double newPace = totalPace / totalCount;
        newPace = Math.round(newPace * 100.0) / 100.0;

        zSet.add(paceRankKey, userId, newPace);
        redisTemplate.expire(paceRankKey, DateUtils.computeDurationForNextWeek());
        redisTemplate.expire(paceHashKey, DateUtils.computeDurationForNextWeek());
    }
}
