package com.ssafy.runit.domain.auth.dto.request;

import com.ssafy.runit.domain.auth.entity.User;
import com.ssafy.runit.domain.group.entity.Group;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserRegisterRequest {
    private String userName;
    private String userImageUrl;
    private String userEmail;

    public User Mapper(Group group) {
        return User.builder()
                .userName(userName)
                .userEmail(userEmail)
                .imageUrl(userImageUrl)
                .userGroup(group)
                .build();
    }

    @Schema(hidden = true)
    public boolean isValid() {
        return userName != null && userImageUrl != null && userEmail != null;
    }
}
