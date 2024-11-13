package com.ssafy.runit.domain.record.dto.response;

import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.split.dto.response.SplitResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RecordGetResponse(
        long id,
        double distance,
        Integer bpm,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<SplitResponse> paceList

) {
    public static RecordGetResponse fromEntity(Record record, List<SplitResponse> paceLs) {
        return RecordGetResponse.builder()
                .id(record.getId())
                .distance(record.getDistance())
                .bpm(record.getBpm())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .paceList(paceLs)
                .build();
    }
}
