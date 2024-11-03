package com.ssafy.runit.domain.record.dto.response;

import com.ssafy.runit.domain.record.entity.Record;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record recordGetResponse(
    long id,
    double distance,
    Integer bpm,
    LocalDateTime startTime,
    LocalDateTime endTime
) {
    public static recordGetResponse fromEntity(Record record){
        return recordGetResponse.builder()
                .id(record.getId())
                .distance(record.getDistance())
                .bpm(record.getBpm())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .build();
    }
}
