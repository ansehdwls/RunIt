package com.ssafy.runit.domain.record.dto.response;

import lombok.Builder;

@Builder
public record RecordTodayResponse(
        Integer distance,
        Long time,
        Integer pace
) {
    public static RecordTodayResponse fromEntity(Integer dis, Long time, Integer pace){
        return RecordTodayResponse.builder()
                .pace(pace)
                .time(time)
                .distance(dis)
                .build();
    }
}
