package com.ssafy.runit.domain.group.dto.response;

import com.ssafy.runit.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetGroupUsersResponse {
    private String userName;
    private String imageUrl;
    private Long exp;

    public static GetGroupUsersResponse fromEntity(User user, Long exp) {
        return GetGroupUsersResponse.builder()
                .userName(user.getUserName())
                .imageUrl(user.getImageUrl())
                .exp(exp)
                .build();
    }
}
