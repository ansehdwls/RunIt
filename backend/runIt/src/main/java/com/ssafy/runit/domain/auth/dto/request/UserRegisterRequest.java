package com.ssafy.runit.domain.auth.dto.request;

import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private String userName;
    private String userImageUrl;
    private String userNumber;

    public User Mapper(Group group) {
        return User.builder()
                .userName(userName)
                .userNumber(userNumber)
                .imageUrl(userImageUrl)
                .userGroup(group)
                .build();
    }

    @Schema(hidden = true)
    public boolean isValid() {
        return userName != null && userImageUrl != null && userNumber != null;
    }
}
