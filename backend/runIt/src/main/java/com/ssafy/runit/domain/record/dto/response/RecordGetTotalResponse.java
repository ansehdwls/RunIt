package com.ssafy.runit.domain.record.dto.response;

import lombok.Builder;

@Builder
public record RecordGetTotalResponse(
        Double totalDistance,
        Long totalTime,
        Double weekDistance,
        Long weekTime
) {
    public static RecordGetTotalResponse fromEntity(Double tDis, Long tTime, Double wDis, Long wTime){
        return RecordGetTotalResponse.builder()
                .totalDistance(tDis)
                .totalTime(tTime)
                .weekDistance(wDis)
                .weekTime(wTime)
                .build();
    }
}
