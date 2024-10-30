package com.ssafy.runit.domain.group.service;

import com.ssafy.runit.domain.group.dto.response.GetGroupUsersResponse;

import java.util.List;

public interface GroupService {

    List<GetGroupUsersResponse> findUsersByGroup(Long GroupId);
}
