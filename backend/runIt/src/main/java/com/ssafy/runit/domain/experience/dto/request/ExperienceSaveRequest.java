package com.ssafy.runit.domain.experience.dto.request;

import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.user.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceSaveRequest {
    private String activity;
    private long changed;

    public Experience Mapper(User user, LocalDateTime createAt) {
        return Experience.builder()
                .activity(activity)
                .changed(changed)
                .createAt(createAt)
                .user(user)
                .build();
    }
}
