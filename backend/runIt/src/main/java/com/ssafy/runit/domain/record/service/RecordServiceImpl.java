package com.ssafy.runit.domain.record.service;

import com.ssafy.runit.domain.pace.dto.response.PaceResponse;
import com.ssafy.runit.domain.pace.entity.Pace;
import com.ssafy.runit.domain.pace.repository.PaceRepository;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.RecordGetListResponse;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.record.repository.RecordRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;
    private final TrackRepository trackRepository;
    private final PaceRepository paceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void saveRunningRecord(UserDetails userDetails, RecordSaveRequest request, MultipartFile file) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        Record record = request.mapper(findUser);

        recordRepository.save(record);
        Record afRecord = request.toEntity(record);

        trackRepository.save(afRecord.getTrack());

        List<Pace> paceList = afRecord.getPaceList();

        for (Pace item : paceList){
            log.debug("item = {} {}", item.getBpm(), item.getDuration());
            paceRepository.save(item);
        }

        log.debug(file.getOriginalFilename());

    }

    @Override
    public RecordGetResponse getRecord(UserDetails userDetails, Long recordId) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        log.debug("user = {}", findUser);

        Record record = recordRepository.findByUserIdAndRecordId(findUser.getId(), recordId).orElseThrow(
                () -> new CustomException(RecordErrorCode.NOT_FOUND_RECORD_DATA)
        );

        List<Pace> pace = paceRepository.findByRecordId(recordId);
        List<PaceResponse> paceResponseList = new ArrayList<>();
        for(Pace item : pace){
            log.debug("item = {} {}", item.getDuration(), item.getBpm());
            paceResponseList.add(PaceResponse.isEntity(item.getBpm(), item.getDuration()));
        }

        return RecordGetResponse.fromEntity(record, paceResponseList);
    }

    @Override
    public List<RecordGetListResponse> getRecordList(UserDetails userDetails) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();
        List<Record> record = recordRepository.findByUserId(findUser.getId());


        List<RecordGetListResponse> recordGetListResponses = new ArrayList<>();

        for(Record item : record){
            Track track = trackRepository.findTrackImageUrlByRecordId(item.getId()).orElseThrow();
            recordGetListResponses.add(RecordGetListResponse.fromEntity(item, findUser.getUserName(), track.getTrackImageUrl()));
        }



        return recordGetListResponses;
    }
}
