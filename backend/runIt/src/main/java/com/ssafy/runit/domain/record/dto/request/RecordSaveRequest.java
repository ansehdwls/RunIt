package com.ssafy.runit.domain.record.dto.request;

import com.ssafy.runit.domain.pace.dto.PaceSaveDto;
import com.ssafy.runit.domain.pace.entity.Pace;
import com.ssafy.runit.domain.record.dto.RecordSaveDto;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.track.dto.TrackSaveDto;
import com.ssafy.runit.domain.track.entity.Track;
import com.ssafy.runit.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordSaveRequest {

    private TrackSaveDto track;
    private RecordSaveDto record;
    private List<PaceSaveDto> paceList;


    public Record mapper(User user){

        Record recordEntity = record.toEntity(user);

        Record recordResult = Record.builder()
                .user(user)
                .bpm(recordEntity.getBpm())
                .distance(recordEntity.getDistance())
                .startTime(recordEntity.getStartTime())
                .endTime(recordEntity.getEndTime())
                .isPractice(recordEntity.getIsPractice())
                .build();


        return recordResult;
    }

    public Record toEntity(Record record, String url){
        List<Pace> paceEntity = paceList.stream()
                        .map(paceSaveDto -> paceSaveDto.toEntity(record))
                        .collect(Collectors.toList());

        Track trackEntity = track.toEntity(record, url);

        Record recordResult = Record.builder()
                .track(trackEntity)
                .paceList(paceEntity)
                .build();

        return recordResult;
    }

}
