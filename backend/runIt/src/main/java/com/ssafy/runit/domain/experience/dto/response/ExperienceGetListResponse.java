package com.ssafy.runit.domain.experience.dto.response;

import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.user.entity.User;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record ExperienceGetListResponse(
        String activity,
        long changed,
        Timestamp createAt
) {

    public static ExperienceGetListResponse fromEntity(Experience exp){
        return ExperienceGetListResponse.builder()
                .activity(exp.getActivity())
                .changed(exp.getChanged())
                .createAt(exp.getCreateAt())
                .build();
    }


}
