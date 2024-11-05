package com.ssafy.runit.domain.record.service;

import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface RecordService {
    void saveRunningRecord(User user, RecordSaveRequest request, MultipartFile file);

    RecordGetResponse getRecord(Long userId, Long recordId);

    List<RecordGetResponse> getRecordList(Long userId);
}
