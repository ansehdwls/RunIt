package com.ssafy.runit.domain.record.service;

import com.ssafy.runit.domain.pace.dto.response.PaceResponse;
import com.ssafy.runit.domain.pace.entity.Pace;
import com.ssafy.runit.domain.pace.repository.PaceRepository;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.*;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.record.repository.RecordRepository;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.TrackErrorCode;
import com.ssafy.runit.util.DateUtils;
import com.ssafy.runit.util.S3UploadUtil;
import com.ssafy.runit.domain.track.entity.Track;
import com.ssafy.runit.domain.track.repository.TrackRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.RecordErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final TrackRepository trackRepository;
    private final PaceRepository paceRepository;
    private final UserRepository userRepository;
    private final S3UploadUtil s3UploadUtil;

    @Override
    @Transactional
    public void saveRunningRecord(UserDetails userDetails, RecordSaveRequest request, MultipartFile file) {
        if (file == null) {
            throw new CustomException(TrackErrorCode.NOT_FOUND_TRACK_IMG);
        }

        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(AuthErrorCode.UNREGISTERED_USER_ERROR)
        );

        Record record = request.mapper(findUser);

        recordRepository.save(record);

        try {
            String url = s3UploadUtil.saveFile(file);
            Record afRecord = request.toEntity(record, url);

            trackRepository.save(afRecord.getTrack());

            List<Pace> paceList = afRecord.getPaceList();

            paceRepository.saveAll(paceList);
        } catch (Exception e) {
            throw new CustomException(TrackErrorCode.NOT_FOUND_TRACK_IMG);
        }
    }

    @Override
    public RecordGetResponse getRecord(UserDetails userDetails, Long recordId) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        Record record = recordRepository.findByUserIdAndRecordId(findUser.getId(), recordId).orElseThrow(
                () -> new CustomException(RecordErrorCode.NOT_FOUND_RECORD_DATA)
        );

        List<PaceResponse> paceResponseList = paceRepository.findByRecordId(recordId)
                .stream()
                .map(item -> PaceResponse.isEntity(item.getBpm(), item.getPace()))
                .collect(Collectors.toList());


        return RecordGetResponse.fromEntity(record, paceResponseList);
    }

    @Override
    public List<RecordGetListResponse> getRecordList(UserDetails userDetails) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        List<RecordGetListResponse> recordGetListResponses = recordRepository.findByUserId(findUser.getId())
                .stream()
                .map(item -> {
                    Track track = trackRepository.findTrackImageUrlByRecordId(item.getId()).orElseThrow();
                    return RecordGetListResponse.fromEntity(item, findUser.getUserName(), track.getTrackImageUrl());

                })
                .collect(Collectors.toList());


        return recordGetListResponses;
    }

    @Override
    public RecordTodayResponse getTodayData(UserDetails userDetails) {
        User user = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        LocalDate localDate = LocalDate.now();


        List<Record> recordList = recordRepository.findByUserIdAndStartTimeBetween(user.getId(), localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay());

        Double dis = 0.0;
        Long time = 0L;
        Integer pace = 0;

        for (Record item : recordList) {
            dis += item.getDistance();

            Duration duration = Duration.between(item.getStartTime(), item.getEndTime());

            long hours = duration.toHours(); // 총 시간 차이
            long minutes = duration.toMinutes() % 60; //

            time += (hours * 60) + minutes;

            pace += item.getBpm();
        }

        return RecordTodayResponse.fromEntity(dis, Long.valueOf(time / recordList.size()).intValue(), pace / recordList.size());
    }

    @Override
    public RecordGetWeekResponse getWeekData(UserDetails userDetails) {
        User user = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        LocalDateTime endTime = LocalDateTime.now();
        LocalDate monday = DateUtils.getLastMonday();
        LocalDateTime startTime = LocalDateTime.of(monday, LocalTime.now());

        List<Record> recordList = recordRepository.findByUserIdAndStartTimeBetween(user.getId(), startTime, endTime);

        /*해당 주에 포함하는 모든 요일을 가져왔음
         * 그러면 이제 다시 요일별로 분류를 해야함
         *
         * */

        List<Double> disList = new ArrayList<>();
        List<Long> timeList = new ArrayList<>();
        List<Double> paceList = new ArrayList<>();


        for (int day = 0; day < 7; day++) {
            LocalDate cur = monday.plusDays(day);

            Long time = 0L;
            Double dis = 0.0;
            Double pace = 0.0;
            Integer cnt = 0;

            for (Record item : recordList) {
                if (item.getStartTime().toString().split("T")[0].equals(cur.toString())) {

                    if (item.getStartTime() != null && item.getEndTime() != null) {
                        time += DateUtils.getSpendTime(item.getStartTime(), item.getEndTime());
                    }

                    dis += item.getDistance();
                    pace += item.getBpm();

                    cnt += 1;
                }
            }

            timeList.add(time);
            disList.add(dis);
            paceList.add(pace / cnt);
        }

        return RecordGetWeekResponse.fromEntity(disList, timeList, paceList);
    }

    @Override
    public RecordGetTotalResponse getTotalData(UserDetails userDetails) {

        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        /*
         * 전체 리스트 들고와서
         *
         * */

        List<Record> recordList = recordRepository.findByUserId(findUser.getId());

        Double totalDis = 0.0;
        Long totalTime = 0L;
        Double weekDis = 0.0;
        Long weekTime = 0L;

        for (Record item : recordList) {
            totalDis += item.getDistance();

            if (item.getStartTime() != null && item.getEndTime() != null) {

                if (item.getStartTime() != null && item.getEndTime() != null) {
                    totalTime += DateUtils.getSpendTime(item.getStartTime(), item.getEndTime());
                }
            }
        }

        LocalDateTime endTime = LocalDateTime.now();
        LocalDate monday = DateUtils.getLastMonday();
        LocalDateTime startTime = LocalDateTime.of(monday, LocalTime.now());

        List<Record> recordWeekList = recordRepository.findByUserIdAndStartTimeBetween(findUser.getId(), startTime, endTime);

        for (Record item : recordWeekList) {
            weekDis += item.getDistance();

            if (item.getStartTime() != null && item.getEndTime() != null) {
                weekTime += DateUtils.getSpendTime(item.getStartTime(), item.getEndTime());
            }
        }

        return RecordGetTotalResponse.fromEntity(totalDis, totalTime, weekDis, weekTime);
    }

    @Override
    public List<RecordGetCalendarResponse> getWeekList(UserDetails userDetails, LocalDate today) {
        User user = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();
        LocalDate monday = DateUtils.getLastMonday(today);
        LocalDate sunday = DateUtils.getLastSunday(today);

        LocalDateTime startTime = LocalDateTime.of(monday, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(sunday, LocalTime.MIN);

        List<Record> recordList = recordRepository.findByUserIdAndStartTimeBetween(user.getId(), startTime, endTime);

        List<RecordGetCalendarResponse> result = new ArrayList<>();

        for (Record item : recordList){
            Track track = trackRepository.getReferenceById(item.getId());
            result.add(RecordGetCalendarResponse.fromEntity(item, user.getUserName(),track.getTrackImageUrl()));
        }

        return result;
    }
}
