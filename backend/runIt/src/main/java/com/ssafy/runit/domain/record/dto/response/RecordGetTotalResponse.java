package com.ssafy.runit.domain.record.dto.response;

import com.ssafy.runit.util.DoubleUtils;
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
                .totalDistance(DoubleUtils.distanceFormatter(tDis))
                .totalTime(tTime)
                .weekDistance(DoubleUtils.distanceFormatter(wDis))
                .weekTime(wTime)
                .build();
    }
}
