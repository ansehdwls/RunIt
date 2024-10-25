package com.ssafy.runit.domain.experience.dto.request;

import com.ssafy.runit.domain.auth.entity.User;
import com.ssafy.runit.domain.experience.entity.Experience;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceSaveRequest {
    private long userId;
    private String activity;
    private long changed;
    private Timestamp createdAt;
    private Date startDate;

    public Experience Mapper(User user){
        return Experience.builder()
                .createAt(createdAt)
                .activity(activity)
                .changed(changed)
                .startDate(startDate)
                .user(user)
                .build();
    }


}
