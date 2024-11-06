package com.ssafy.runit.domain.record.dto.response;

import com.ssafy.runit.domain.record.entity.Record;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecordGetListResponse(
        long id,
        double distance,
        Integer bpm,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String name,
        String imageUrl

) {
    public static RecordGetListResponse fromEntity(Record record, String name, String imageUrl){
        return RecordGetListResponse.builder()
                .id(record.getId())
                .distance(record.getDistance())
                .bpm(record.getBpm())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .imageUrl(imageUrl)
                .name(name)
                .build();
    }
}
