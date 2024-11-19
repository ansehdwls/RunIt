package com.ssafy.runit.domain.record.dto;

import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecordSaveDto {
    private Double distance;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer bpm;
    private Integer duration;

    public Record toEntity(User user){
        return Record.builder()
                .user(user)
                .bpm(bpm)
                .distance(distance)
                .startTime(startTime)
                .endTime(endTime)
                .isPractice(false)
                .duration(duration)
                .build();

    }
}
