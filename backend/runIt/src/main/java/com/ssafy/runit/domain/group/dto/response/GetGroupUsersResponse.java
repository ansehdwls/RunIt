package com.ssafy.runit.domain.group.dto.response;

import com.ssafy.runit.domain.user.entity.User;
import lombok.Builder;

@Builder
public record GetGroupUsersResponse(
        String userName,
        String imageUrl,
        Long exp
) {

    public static GetGroupUsersResponse fromEntity(User user, Long exp) {
        return GetGroupUsersResponse.builder()
                .userName(user.getUserName())
                .imageUrl(user.getImageUrl())
                .exp(exp)
                .build();
    }
}
