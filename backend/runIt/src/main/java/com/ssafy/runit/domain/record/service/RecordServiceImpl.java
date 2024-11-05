package com.ssafy.runit.domain.record.service;

import com.ssafy.runit.domain.pace.entity.Pace;
import com.ssafy.runit.domain.pace.repository.PaceRepository;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.record.repository.RecordRepository;
import com.ssafy.runit.domain.track.dto.TrackSaveDto;
import com.ssafy.runit.domain.track.entity.Track;
import com.ssafy.runit.domain.track.repository.TrackRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.RecordErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    @Transactional
    public void saveRunningRecord(User user, RecordSaveRequest request, MultipartFile file) {
        Record record = request.mapper(user);

        recordRepository.save(record);
        Record afRecord = request.toEntity(record);

        trackRepository.save(afRecord.getTrack());

        List<Pace> paceList = afRecord.getPaceList();

        for (Pace item : paceList){
            paceRepository.save(item);
        }

        log.debug(file.getOriginalFilename());

    }

    @Override
    public RecordGetResponse getRecord(Long userId, Long recordId) {
        Record record = recordRepository.findByUserIdAndRecordId(userId, recordId);
        return RecordGetResponse.fromEntity(record).orElseThrow(
                () -> new CustomException(RecordErrorCode.NOT_FOUND_RECORD_DATA)
        );
    }

    @Override
    public List<RecordGetResponse> getRecordList(Long userId) {
        return recordRepository.findByUserId(userId);
    }
}
