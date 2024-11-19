package com.ssafy.runit.domain.user.dto.response;

import com.ssafy.runit.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private long userId;
    private String userNumber;
    private String userName;
    private String imageUrl;
    private long groupId;

    public static UserInfoResponse fromEntity(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .userNumber(user.getUserNumber())
                .userName(user.getUserName())
                .imageUrl(user.getImageUrl())
                .groupId(user.getUserGroup().getId())
                .build();
    }
}