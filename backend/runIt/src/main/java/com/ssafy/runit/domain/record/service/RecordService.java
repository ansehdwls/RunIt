package com.ssafy.runit.domain.record.service;

import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.user.entity.User;

import java.util.List;

public interface RecordService {
    void saveRunningRecord(User user, RecordSaveRequest request);

    Record getRecord(Long userId);

    List<Record> getRecordList(Long userId);


}
