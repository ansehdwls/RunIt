package com.ssafy.runit.domain.record.dto.response;

import com.ssafy.runit.domain.record.entity.Record;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecordGetCalendarResponse(
        long id,
        double distance,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String name,
        String imageUrl
) {
    public static RecordGetCalendarResponse fromEntity(Record record, String userName, String url) {
        return RecordGetCalendarResponse.builder()
                .distance(record.getDistance())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .name(userName)
                .imageUrl(url)
                .build();
    }
}
