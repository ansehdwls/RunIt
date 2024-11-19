package com.ssafy.runit.domain.record.dto.request;

import com.ssafy.runit.domain.record.dto.RecordSaveDto;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.split.dto.SplitSaveDto;
import com.ssafy.runit.domain.split.entity.Split;
import com.ssafy.runit.domain.track.dto.TrackSaveDto;
import com.ssafy.runit.domain.track.entity.Track;
import com.ssafy.runit.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordSaveRequest {

    private TrackSaveDto track;
    private RecordSaveDto record;
    private List<SplitSaveDto> paceList;


    public Record mapper(User user) {

        Record recordEntity = record.toEntity(user);

        Record recordResult = Record.builder()
                .user(user)
                .duration(recordEntity.getDuration())
                .bpm(recordEntity.getBpm())
                .distance(recordEntity.getDistance())
                .startTime(recordEntity.getStartTime())
                .endTime(recordEntity.getEndTime())
                .isPractice(recordEntity.getIsPractice())
                .build();


        return recordResult;
    }

    public Record toEntity(Record record, String url) {
        List<Split> splitEntity = paceList.stream()
                .map(splitSaveDto -> splitSaveDto.toEntity(record))
                .collect(Collectors.toList());

        Track trackEntity = track.toEntity(record, url);

        Record recordResult = Record.builder()
                .track(trackEntity)
                .splitList(splitEntity)
                .build();

        return recordResult;
    }

}
