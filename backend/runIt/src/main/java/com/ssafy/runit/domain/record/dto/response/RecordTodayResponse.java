package com.ssafy.runit.domain.record.dto.response;

import com.ssafy.runit.util.DoubleUtils;
import lombok.Builder;

@Builder
public record RecordTodayResponse(
        Double distance,
        Long time,
        Double pace
) {
    public static RecordTodayResponse fromEntity(Double dis, Long time, Double pace){
        return RecordTodayResponse.builder()
                .pace(DoubleUtils.distanceFormatter(pace))
                .time(time)
                .distance(DoubleUtils.distanceFormatter(dis))
                .build();
    }
}
