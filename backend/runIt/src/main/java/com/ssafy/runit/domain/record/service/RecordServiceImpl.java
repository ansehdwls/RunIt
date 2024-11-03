package com.ssafy.runit.domain.record.service;

import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.recordGetResponse;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.record.repository.RecordRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.RecordErrorCode;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;

    @Override
    @Transactional
    public void saveRunningRecord(User user, RecordSaveRequest request) {
        Record record = request.mapper(user);
        recordRepository.save(record);
    }

    @Override
    public recordGetResponse getRecord(Long userId) {
        return recordRepository.recordFindByUserId(userId);
    }

    @Override
    public List<recordGetResponse> getRecordList(Long userId) {
        return recordRepository.recordListFindByUserId(userId);
    }
}
