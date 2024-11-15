package com.ssafy.runit.domain.experience.dto.response;

import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.util.DateUtils;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExperienceGetListResponse(
        String activity,
        long changed,
        String createAt
) {

    public static ExperienceGetListResponse fromEntity(Experience exp) {
        return ExperienceGetListResponse.builder()
                .activity(exp.getActivity())
                .changed(exp.getChanged())
                .createAt(DateUtils.trimToSeconds(exp.getCreateAt()).split(" ")[0])
                .build();
    }
}
