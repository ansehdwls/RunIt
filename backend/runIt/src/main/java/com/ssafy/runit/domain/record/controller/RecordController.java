package com.ssafy.runit.domain.record.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.record.service.RecordService;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

    private final UserRepository userRepository;
    private final RecordService recordService;

    @Override
    @PostMapping(value = "/run", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RunItApiResponse<Void> saveRecord(UserDetails userDetails, RecordSaveRequest recordSaveRequest, MultipartFile file) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();
        recordService.saveRunningRecord(findUser, recordSaveRequest, file);
        return new RunItApiResponse<>(null, "성공");
    }

    @Override
    @GetMapping("/run/{recordId}")
    public RunItApiResponse<Optional<RecordGetResponse>> recordFindOne(UserDetails userDetails, Long recordId) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();
        Optional<RecordGetResponse> record = recordService.getRecord(findUser.getId(), recordId);

        return new RunItApiResponse<>(record, "성공");
    }

    @Override
    @GetMapping("/run")
    public RunItApiResponse<List<RecordGetResponse>> recordFindList(UserDetails userDetails) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();
        List<RecordGetResponse> recordList = recordService.getRecordList(findUser.getId());
        return new RunItApiResponse<>(recordList, "성공");
    }
}
