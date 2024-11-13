package com.ssafy.runit.domain.split.dto.response;

import lombok.Builder;

@Builder
public record SplitResponse(
        Integer bpmList,
        Integer durationList
) {
    public static SplitResponse isEntity(Integer bpmLs, Integer durationLs) {
        return SplitResponse.builder()
                .bpmList(bpmLs)
                .durationList(durationLs)
                .build();
    }
}
