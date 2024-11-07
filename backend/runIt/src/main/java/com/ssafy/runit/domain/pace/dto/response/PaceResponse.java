package com.ssafy.runit.domain.pace.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PaceResponse(
    Integer bpmList,
    Integer durationList
) {
    public static PaceResponse isEntity(Integer bpmLs, Integer durationLs){
        return PaceResponse.builder()
                .bpmList(bpmLs)
                .durationList(durationLs)
                .build();
    }
}
