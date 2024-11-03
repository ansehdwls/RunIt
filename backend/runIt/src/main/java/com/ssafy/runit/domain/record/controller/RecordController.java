package com.ssafy.runit.domain.record.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.record.service.RecordService;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecordController implements RecordDocs{

    private final UserRepository userRepository;
    private final RecordService recordService;

    @Override
    @PostMapping("/run")
    public RunItApiResponse<Void> saveRecord(UserDetails userDetails, RecordSaveRequest recordSaveRequest) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();
        recordService.saveRunningRecord(findUser, recordSaveRequest);
        return new RunItApiResponse<>(null, "성공");
    }

    @Override
    @GetMapping("")
    public RunItApiResponse<RecordGetResponse> recordFindOne(UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/run")
    public RunItApiResponse<List<RecordGetResponse>> recordFindList(UserDetails userDetails) {
        return null;
    }
}
