package com.ssafy.runit.domain.record.dto.response;

import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.util.DoubleUtils;
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
        String imageUrl,
        double avgPace

) {
    public static RecordGetListResponse fromEntity(Record record, String name, String imageUrl, double avgPace){
        return RecordGetListResponse.builder()
                .id(record.getId())
                .distance(DoubleUtils.distanceFormatter(record.getDistance()))
                .bpm(record.getBpm())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .imageUrl(imageUrl)
                .name(name)
                .avgPace(avgPace)
                .build();
    }
}
