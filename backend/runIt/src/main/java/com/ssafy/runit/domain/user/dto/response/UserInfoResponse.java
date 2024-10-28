package com.ssafy.runit.domain.user.dto.response;

import com.ssafy.runit.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserInfoResponse(
        long userId,
        String userEmail,
        String userName,
        String imageUrl,
        long groupId) {

    public static UserInfoResponse fromEntity(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .imageUrl(user.getImageUrl())
                .groupId(user.getUserGroup().getId())
                .build();
    }
}