package com.ssafy.runit.domain.group.service;

import com.ssafy.runit.domain.group.dto.response.GetGroupUsersInfo;

public interface GroupService {

    GetGroupUsersInfo findUsersByGroupWithRank(Long GroupId, String userNumber, String RankType);
}
