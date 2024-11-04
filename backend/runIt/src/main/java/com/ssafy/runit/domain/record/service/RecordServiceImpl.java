package com.ssafy.runit.domain.record.service;

import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.record.repository.RecordRepository;
import com.ssafy.runit.domain.track.entity.Track;
import com.ssafy.runit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;

    @Override
    @Transactional
    public void saveRunningRecord(User user, RecordSaveRequest request) {
        Record record = request.mapper(user);
        Track track = record.getTrack();
        log.debug("track = {}", track.getTrackImageUrl());
        recordRepository.save(record);

    }

    @Override
    public Optional<RecordGetResponse> getRecord(Long userId) {
        return recordRepository.findFirstByUserId(userId);
    }

    @Override
    public List<RecordGetResponse> getRecordList(Long userId) {
        return recordRepository.findByUserId(userId);
    }


}
