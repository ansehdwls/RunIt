package com.ssafy.runit.domain.record.dto.response;

import lombok.Builder;

@Builder
public record RecordTodayResponse(
        Double distance,
        Long time,
        Double pace
) {
    public static RecordTodayResponse fromEntity(Double dis, Long time, Double pace){
        return RecordTodayResponse.builder()
                .pace(pace)
                .time(time)
                .distance(dis)
                .build();
    }
}
