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
public class GroupUserInfo {
    private String userName;
    private String imageUrl;
    private String score;
    private Integer rankDiff;

    public static GroupUserInfo fromEntity(User user, String exp, int diff) {
        return GroupUserInfo.builder()
                .userName(user.getUserName())
                .imageUrl(user.getImageUrl())
                .score(exp)
                .rankDiff(diff)
                .build();
    }
}
