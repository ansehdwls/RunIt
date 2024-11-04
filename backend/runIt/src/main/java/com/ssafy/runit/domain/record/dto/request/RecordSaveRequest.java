package com.ssafy.runit.domain.record.dto.request;

import com.ssafy.runit.domain.pace.entity.Pace;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.track.entity.Track;
import com.ssafy.runit.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordSaveRequest {

    private Track track;
    private double distance;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int bpm;
    private List<Pace> paceList;

    public Record mapper(User user){
        return Record.builder()
                .bpm(bpm)
                .track(track)
                .paceList(paceList)
                .distance(distance)
                .startTime(startTime)
                .endTime(endTime)
                .user(user)
                .build();
    }

}
