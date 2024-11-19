package com.ssafy.runit.domain.rank.service;

import com.ssafy.runit.config.redis.RedisKeys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AveragePaceRankingService extends BaseRankingService<Double> {

    public AveragePaceRankingService(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getRankKey(String groupId) {
        return RedisKeys.getPaceRankKey(groupId);
    }

    @Override
    protected String getPreviousRankKey(String groupId) {
        return RedisKeys.getPacePreviousRankKey(groupId);
    }

    @Override
    protected double calculateScore(Double data) {
        return data;
    }

    public String getSubKey(String userId) {
        return RedisKeys.getUserPaceKey(userId);
    }

    @Override
    protected boolean isDescendingOrder() {
        return false;
    }
}
