package com.ssafy.runit.domain.record.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.attendance.dto.AttendanceSaveDto;
import com.ssafy.runit.domain.attendance.entity.Attendance;
import com.ssafy.runit.domain.attendance.service.AttendanceService;
import com.ssafy.runit.domain.experience.service.ExperienceService;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.*;
import com.ssafy.runit.domain.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecordController implements RecordDocs{

    private final RecordService recordService;
    private final AttendanceService attendanceService;

    @Override
    @PostMapping(value = "/run", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RunItApiResponse<RecordPostResponse> saveRecord(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestPart(value = "dto")  RecordSaveRequest recordSaveRequest,
                                                           @RequestPart(value = "images", required = false) MultipartFile file) {

        recordService.saveRunningRecord(userDetails, recordSaveRequest, file);

        /*
        * 경험치 연산 로직 추가
        * 출석은 10점인데
        * 5일 출석은 + 50
        * 시점은 4 -> 5로 넘어갈 때
        * */


        /*
         * 당일 참석에 아무것도 없으면 넣어줘야함
         * */


        RecordPostResponse postResponse;



        if (attendanceService.getTodayAttended(LocalDate.now()) == false){
            // 출석 + 경험치
            postResponse = RecordPostResponse.toEntity(false, 100);
            attendanceService.saveAttendance(userDetails);
        }
        else{
            postResponse = RecordPostResponse.toEntity(true, 100);
        }

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
    public RunItApiResponse<List<RecordGetCalendarResponse>> recordFindWeekList(UserDetails userDetails, LocalDate today) {
        List<RecordGetCalendarResponse> response = recordService.getWeekList(userDetails, today);
        return new RunItApiResponse<>(response, "성공");
    }
}
