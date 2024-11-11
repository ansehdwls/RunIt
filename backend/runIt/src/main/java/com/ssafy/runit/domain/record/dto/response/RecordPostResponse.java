package com.ssafy.runit.domain.record.dto.response;

import lombok.Builder;

@Builder
public record RecordPostResponse(
        Long id,
        Boolean isAttend,
        Integer exp
) {
    public static RecordPostResponse toEntity(Long recordId,Boolean attend, Integer exp){
        return RecordPostResponse.builder()
                .id(recordId)
                .exp(exp)
                .isAttend(attend)
                .build();
    }
}
