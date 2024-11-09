package com.ssafy.runit.domain.record.dto.response;

import lombok.Builder;

@Builder
public record RecordPostResponse(
        Boolean isAttend,
        Integer exp
) {
    public static RecordPostResponse toEntity(Boolean attend, Integer exp){
        return RecordPostResponse.builder()
                .exp(exp)
                .isAttend(attend)
                .build();
    }
}
