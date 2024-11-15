package com.ssafy.runit.domain.record.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.attendance.service.AttendanceService;
import com.ssafy.runit.domain.experience.service.ExperienceService;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.*;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.record.service.RecordService;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.RecordErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecordController implements RecordDocs {

    private final RecordService recordService;
    private final AttendanceService attendanceService;
    private final ExperienceService experienceService;

    @Override
    @PostMapping(value = "/run", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RunItApiResponse<RecordPostResponse> saveRecord(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestPart(value = "dto") RecordSaveRequest recordSaveRequest,
                                                           @RequestPart(value = "images") MultipartFile file) {
//        if (recordSaveRequest.getRecord().getDistance() < 0.1){
//            throw new CustomException(RecordErrorCode.NOT_ALLOW_DISTANCE);
//        }

        Record record = recordService.saveRunningRecord(userDetails, recordSaveRequest, file);

        boolean attendanceType = attendanceService.getTodayAttended(userDetails, LocalDate.now());

        RecordPostResponse postResponse = RecordPostResponse.toEntity(
                record.getId(),
                attendanceType,
                experienceService.experienceDistribution(userDetails, attendanceType)
        );

        return new RunItApiResponse<>(postResponse, "성공");
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

    @Override
    @GetMapping("/run/practice")
    public RunItApiResponse<List<RecordGetListResponse>> recordFindPractiseList(UserDetails userDetails) {
        List<RecordGetListResponse> responseList = recordService.getRecordPracList(userDetails);
        return new RunItApiResponse<>(responseList, "성공");
    }

    @Override
    @GetMapping("/run/today")
    public RunItApiResponse<RecordTodayResponse> recordFindToday(UserDetails userDetails) {
        RecordTodayResponse todayResponse = recordService.getTodayData(userDetails);
        return new RunItApiResponse<>(todayResponse, "성공");
    }

    @Override
    @GetMapping("/run/week")
    public RunItApiResponse<RecordGetWeekResponse> recordFindWeek(UserDetails userDetails) {
        RecordGetWeekResponse weekResponse = recordService.getWeekData(userDetails);
        return new RunItApiResponse<>(weekResponse, "성공");
    }

    @Override
    @GetMapping("/run/total")
    public RunItApiResponse<RecordGetTotalResponse> recordFindTotal(UserDetails userDetails) {
        RecordGetTotalResponse totalResponse = recordService.getTotalData(userDetails);
        return new RunItApiResponse<>(totalResponse, "성공");
    }

    @Override
    @GetMapping("/run/weekList/{today}")
    public RunItApiResponse<List<List<RecordGetListResponse>>> recordFindWeekList(UserDetails userDetails, LocalDate today) {
        List<List<RecordGetListResponse>> response = recordService.getWeekList(userDetails, today);
        return new RunItApiResponse<>(response, "성공");
    }

    @Override
    @PutMapping("/run/record/{recordId}")
    public RunItApiResponse<Void> recordPracticeUpdate(UserDetails userDetails, Long recordId) {
        recordService.putRecord(userDetails, recordId);
        return new RunItApiResponse<>(null, "성공");
    }


}
