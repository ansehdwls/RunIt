package com.ssafy.runit.domain.record.service;

import com.ssafy.runit.domain.rank.service.DistanceRankManager;
import com.ssafy.runit.domain.rank.service.PaceRankManager;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.*;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.record.repository.RecordRepository;
import com.ssafy.runit.domain.split.dto.response.SplitResponse;
import com.ssafy.runit.domain.split.entity.Split;
import com.ssafy.runit.domain.split.repository.SplitRepository;
import com.ssafy.runit.domain.track.repository.TrackRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.RecordErrorCode;
import com.ssafy.runit.exception.code.TrackErrorCode;
import com.ssafy.runit.util.DateUtils;
import com.ssafy.runit.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final TrackRepository trackRepository;
    private final SplitRepository splitRepository;
    private final UserRepository userRepository;
    private final S3UploadUtil s3UploadUtil;
    private final PaceRankManager paceRankManager;
    private final DistanceRankManager distanceRankManager;

    @Override
    @Transactional
    public Record saveRunningRecord(UserDetails userDetails, RecordSaveRequest request, MultipartFile file) {

        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(AuthErrorCode.UNREGISTERED_USER_ERROR)
        );
        log.debug("user-group : {}", findUser.getUserGroup().getId());
        Record record = request.mapper(findUser);

        recordRepository.save(record);

        try {
            String url = s3UploadUtil.saveFile(file, findUser.getId(), record.getId());
            Record afRecord = request.toEntity(record, url);
            trackRepository.save(afRecord.getTrack());
            record.updateSplitList(afRecord.getSplitList());
            splitRepository.saveAll(afRecord.getSplitList());
            paceRankManager.updatePace(record, String.valueOf(findUser.getUserGroup().getId()), String.valueOf(findUser.getId()));
            distanceRankManager.updateDistance(findUser.getUserGroup().getId(), String.valueOf(findUser.getId()), record.getDistance());
        } catch (RuntimeException e) {
            throw e;
        } catch (IOException e) {
            throw new CustomException(TrackErrorCode.NOT_FOUND_TRACK_IMG);
        }

        return record;
    }

    @Override
    public RecordGetResponse getRecord(UserDetails userDetails, Long recordId) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        Record record = recordRepository.findByUserIdAndRecordId(findUser.getId(), recordId).orElseThrow(
                () -> new CustomException(RecordErrorCode.NOT_FOUND_RECORD_DATA)
        );

        List<SplitResponse> splitResponseList = splitRepository.findByRecordId(recordId)
                .stream()
                .map(item -> SplitResponse.isEntity(item.getBpm(), item.getPace()))
                .collect(Collectors.toList());


        return RecordGetResponse.fromEntity(record, splitResponseList);
    }

    @Override
    public List<RecordGetListResponse> getRecordList(UserDetails userDetails) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();


        return recordRepository.findByUserId(findUser.getId())
                .stream()
                .map(item -> {

                    return RecordGetListResponse.fromEntity(item, findUser.getUserName(), item.getTrack().getTrackImageUrl());

                })
                .collect(Collectors.toList());
    }

    @Override
    public RecordTodayResponse getTodayData(UserDetails userDetails) {
        User user = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        LocalDate localDate = LocalDate.now();


        List<Record> recordList = recordRepository.findByUserIdAndStartTimeBetween(user.getId(), localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay());

        double dis = 0;
        long time = 0L;
        double pace = 0;


        for (Record item : recordList) {
            dis += item.getDistance();

            Duration duration = Duration.between(item.getStartTime(), item.getEndTime());

            long hours = duration.toHours(); // 총 시간 차이
            long minutes = duration.toMinutes(); //

            time += (hours * 60) + minutes;

            if(!item.getSplitList().isEmpty()) {
                pace += item.getSplitList().stream()
                        .mapToDouble(Split::getPace) // 각 요소에서 특정 속성 값을 추출하여 더함
                        .sum() / item.getSplitList().size();
            }
            log.debug("item size{} record size {}", item.getSplitList().size(), recordList.size());
        }

        if (recordList.isEmpty()) {
            return RecordTodayResponse.fromEntity(0.0, 0L, 0.0);
        } else {
            return RecordTodayResponse.fromEntity(dis, time, pace / recordList.size()/60);
        }
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

            long time = 0L;
            double dis = 0.0;
            double pace = 0;
            int cnt = 0;

            for (Record item : recordList) {
                if (item.getStartTime().toString().split("T")[0].equals(cur.toString())) {

                    if (item.getEndTime() != null) {
                        time += DateUtils.getSpendTime(item.getStartTime(), item.getEndTime());
                    }

                    dis += item.getDistance();
                    if(!item.getSplitList().isEmpty()) {
                        pace += item.getSplitList().stream()
                                .mapToDouble(Split::getPace) // 각 요소에서 특정 속성 값을 추출하여 더함
                                .sum() / item.getSplitList().size();
                    }

                    cnt += 1;
                }
            }

            timeList.add(time);
            disList.add(dis);

            if (cnt > 0) {
                paceList.add(pace / cnt / 60);
            } else {
                paceList.add(pace);
            }
        }

        return RecordGetWeekResponse.fromEntity(disList, timeList, paceList);
    }

    @Override
    public RecordGetTotalResponse getTotalData(UserDetails userDetails) {

        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        List<Record> recordList = recordRepository.findByUserId(findUser.getId());

        double totalDis = 0.0;
        long totalTime = 0L;
        double weekDis = 0.0;
        long weekTime = 0L;

        for (Record item : recordList) {
            totalDis += item.getDistance();
            if (item.getStartTime() != null && item.getEndTime() != null) {
                totalTime += DateUtils.getSpendTime(item.getStartTime(), item.getEndTime());
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
    public List<List<RecordGetListResponse>> getWeekList(UserDetails userDetails, LocalDate today) {
        User user = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        LocalDate monday = DateUtils.getLastMonday(today);
        LocalDate sunday = DateUtils.getLastSunday(monday);


        LocalDateTime startTime = LocalDateTime.of(monday, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(sunday, LocalTime.MIN);

        List<Record> recordList = recordRepository.findByUserIdAndStartTimeBetween(user.getId(), startTime, endTime);

        List<List<RecordGetListResponse>> result = Arrays.asList(
                new ArrayList<>(), // Monday
                new ArrayList<>(), // Tuesday
                new ArrayList<>(), // Wednesday
                new ArrayList<>(), // Thursday
                new ArrayList<>(), // Friday
                new ArrayList<>(), // Saturday
                new ArrayList<>()  // Sunday
        );


        for (Record item : recordList) {

            LocalDate itemDate = item.getStartTime().toLocalDate();

            // 날짜를 기준으로 요일 인덱스를 계산
            int dayIndex = (int) ChronoUnit.DAYS.between(monday, itemDate) % 7;

            // 요일 인덱스가 0 이상 6 이하인지 확인
            if (dayIndex >= 0) {
                RecordGetListResponse toRecord = RecordGetListResponse.fromEntity(
                        item, user.getUserName(), item.getTrack().getTrackImageUrl());

                // 요일 인덱스에 맞는 리스트에 추가
                result.get(dayIndex).add(toRecord);
            }
        }

        return result;
    }

    @Override
    public List<RecordGetListResponse> getRecordPracList(UserDetails userDetails) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        return recordRepository.findByUserId(findUser.getId())
                .stream()
                .filter(item -> item.getIsPractice())
                .map(item -> {
                    return RecordGetListResponse.fromEntity(item, findUser.getUserName(), item.getTrack().getTrackImageUrl());
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void putRecord(UserDetails userDetails, Long recordId) {
        User user = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        recordRepository.findByUserIdAndRecordId(user.getId(), recordId)
                .ifPresentOrElse(
                        record -> recordRepository.updateRecordPractice(recordId, !record.getIsPractice()),
                        () -> {
                            throw new CustomException(RecordErrorCode.NOT_FOUND_RECORD_DATA);
                        }

                );
    }
}
