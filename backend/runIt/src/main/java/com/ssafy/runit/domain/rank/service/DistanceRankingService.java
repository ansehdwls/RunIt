package com.ssafy.runit.domain.rank.service;

import com.ssafy.runit.config.redis.RedisKeys;
import com.ssafy.runit.domain.rank.UpdateType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DistanceRankingService extends BaseRankingService<Double> {

    public DistanceRankingService(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getRankKey(String userId) {
        return RedisKeys.getDistanceRankKey(userId);
    }

    @Override
    protected String getPreviousRankKey(String userId) {
        return RedisKeys.getDistancePreviousRankKey(userId);
    }

    @Override
    protected double calculateScore(Double data) {
        return data;
    }

    @Override
    protected UpdateType getUpdateType() {
        return UpdateType.INCREMENT;
    }
}
