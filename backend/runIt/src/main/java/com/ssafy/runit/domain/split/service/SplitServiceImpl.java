package com.ssafy.runit.domain.split.service;

import com.ssafy.runit.domain.rank.service.PaceRankManager;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.split.repository.SplitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SplitServiceImpl implements SplitService {

    private final SplitRepository splitRepository;
    private final PaceRankManager paceRankManager;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    @Override
    public void saveAllSplit(Record record) {
        String groupId = String.valueOf(record.getUser().getUserGroup().getId());
        String userId = String.valueOf(record.getUser().getId());
        splitRepository.saveAll(record.getSplitList());
        paceRankManager.updatePace(record, groupId, userId);
        //TODO 거리 저장 로직 추가
    }
}
