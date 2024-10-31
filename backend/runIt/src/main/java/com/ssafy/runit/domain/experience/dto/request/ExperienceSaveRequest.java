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
    private long userId;
    private String activity;
    private long changed;
    private LocalDateTime createdAt;
    private LocalDate startDate;

    public Experience Mapper(User user) {
        return Experience.builder()
                .createAt(createdAt)
                .activity(activity)
                .changed(changed)
                .startDate(startDate)
                .user(user)
                .build();
    }
}
