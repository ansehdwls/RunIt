package com.ssafy.runit.domain.record.service;

import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.*;
import com.ssafy.runit.domain.record.entity.Record;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordService {
    Record saveRunningRecord(UserDetails userDetails, RecordSaveRequest request, MultipartFile file);

    RecordGetResponse getRecord(UserDetails userDetails, Long recordId);

    List<RecordGetListResponse> getRecordList(UserDetails userDetails);

    RecordTodayResponse getTodayData(UserDetails userDetails);

    RecordGetWeekResponse getWeekData(UserDetails userDetails);

    RecordGetTotalResponse getTotalData(UserDetails userDetails);

    List<List<RecordGetListResponse>> getWeekList(UserDetails userDetails, LocalDate today);

    List<RecordGetListResponse> getRecordPracList(UserDetails userDetails);

    void putRecord(UserDetails userDetails,Long recordId);
}
