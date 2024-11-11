package com.ssafy.runit.domain.record.dto.response;

import lombok.Builder;

@Builder
public record RecordTodayResponse(
        Double distance,
        Integer time,
        Integer pace
) {
    public static RecordTodayResponse fromEntity(Double dis, Integer time, Integer pace){
        return RecordTodayResponse.builder()
                .pace(pace)
                .time(time)
                .distance(dis)
                .build();
    }
}
