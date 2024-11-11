package com.ssafy.runit.domain.record.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RecordGetWeekResponse(
        List<Double> disList,
        List<Long> timeList,
        List<Double> paceList
) {
    public static RecordGetWeekResponse fromEntity(List<Double> disList,
                                                   List<Long> timeList,
                                                   List<Double> paceList) {
        return RecordGetWeekResponse.builder()
                .disList(disList)
                .paceList(paceList)
                .timeList(timeList)
                .build();
    }
}
