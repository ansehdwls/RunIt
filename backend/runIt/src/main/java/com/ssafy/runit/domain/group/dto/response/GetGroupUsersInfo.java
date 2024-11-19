package com.ssafy.runit.domain.group.dto.response;

import com.ssafy.runit.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetGroupUsersInfo {
    private List<GroupUserInfo> userInfos;
    private long rank;
    private int leagueRank;

    public GetGroupUsersInfo Mapper(List<GroupUserInfo> userInfos, User user, int leagueRank) {
        int idx = 0;
        int userRank = 0;
        for (GroupUserInfo groupUserInfo : userInfos) {
            idx++;
            if (groupUserInfo.getUserName().equals(user.getUserName()) && groupUserInfo.getImageUrl().equals(user.getImageUrl())) {
                userRank = idx;
                break;
            }
        }
        return GetGroupUsersInfo.builder()
                .userInfos(userInfos)
                .rank(userRank)
                .leagueRank(leagueRank)
                .build();
    }
}
