package com.ssafy.runit.domain.rank.service;

import com.ssafy.runit.config.redis.RedisKeys;
import com.ssafy.runit.domain.rank.UpdateType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExperiencePointRankingService extends BaseRankingService<Long> {

    public ExperiencePointRankingService(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getRankKey(String groupId) {
        return RedisKeys.getExperienceRankKey(groupId);
    }

    @Override
    protected String getPreviousRankKey(String groupId) {
        return RedisKeys.getExperiencePreviousRankKey(groupId);
    }

    @Override
    protected double calculateScore(Long data) {
        return data.doubleValue();
    }

    @Override
    protected UpdateType getUpdateType() {
        return UpdateType.INCREMENT;
    }

    @Override
    protected boolean isDescendingOrder() {
        return true;
    }
}
