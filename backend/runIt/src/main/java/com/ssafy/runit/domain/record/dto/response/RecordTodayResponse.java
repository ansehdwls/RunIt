package com.ssafy.runit.domain.record.dto.response;

import lombok.Builder;

@Builder
public record RecordTodayResponse(
        Double distance,
        Long time,
        Integer pace
) {
    public static RecordTodayResponse fromEntity(Double dis, Long time, Integer pace){
        return RecordTodayResponse.builder()
                .pace(pace)
                .time(time)
                .distance(dis)
                .build();
    }
}
