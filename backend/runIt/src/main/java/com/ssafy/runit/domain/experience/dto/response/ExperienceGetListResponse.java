package com.ssafy.runit.domain.experience.dto.response;

import com.ssafy.runit.domain.experience.entity.Experience;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExperienceGetListResponse(
        String activity,
        long changed,
        LocalDateTime createAt
) {

    public static ExperienceGetListResponse fromEntity(Experience exp) {
        return ExperienceGetListResponse.builder()
                .activity(exp.getActivity())
                .changed(exp.getChanged())
                .createAt(exp.getCreateAt())
                .build();
    }
}
