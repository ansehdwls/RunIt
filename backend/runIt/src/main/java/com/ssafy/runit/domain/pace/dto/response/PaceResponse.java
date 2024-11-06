package com.ssafy.runit.domain.pace.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PaceResponse(
    Double bpmList,
    Double durationList
) {
    public static PaceResponse isEntity(Double bpmLs, Double durationLs){
        return PaceResponse.builder()
                .bpmList(bpmLs)
                .durationList(durationLs)
                .build();
    }
}
