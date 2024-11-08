package com.ssafy.runit.domain.group.service;

import com.ssafy.runit.domain.group.dto.response.GetGroupUsersInfo;
import com.ssafy.runit.domain.group.dto.response.GroupUserInfo;
import com.ssafy.runit.domain.group.entity.Group;

import java.util.List;

public interface GroupService {

    GetGroupUsersInfo findUsersByGroupWithRank(Long GroupId, String userNumber);

    List<GroupUserInfo> findUsersByGroup(Group group);
}
