package com.ssafy.runit.domain.record.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.RecordGetListResponse;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.record.service.RecordService;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecordController implements RecordDocs{

    private final RecordService recordService;

    @Override
    @PostMapping(value = "/run", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RunItApiResponse<Void> saveRecord(@AuthenticationPrincipal UserDetails userDetails, RecordSaveRequest recordSaveRequest, MultipartFile file) {
        recordService.saveRunningRecord(userDetails, recordSaveRequest, file);
        return new RunItApiResponse<>(null, "성공");
    }

    @Override
    @GetMapping("/run/{recordId}")
    public RunItApiResponse<RecordGetResponse> recordFindOne(@AuthenticationPrincipal UserDetails userDetails, Long recordId) {
        RecordGetResponse record = recordService.getRecord(userDetails, recordId);

        return new RunItApiResponse<>(record, "성공");
    }

    @Override
    @GetMapping("/run")
    public RunItApiResponse<List<RecordGetListResponse>> recordFindList(@AuthenticationPrincipal UserDetails userDetails) {
        List<RecordGetListResponse> recordList = recordService.getRecordList(userDetails);
        return new RunItApiResponse<>(recordList, "성공");
    }
}
