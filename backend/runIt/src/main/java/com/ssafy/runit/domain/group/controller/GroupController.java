package com.ssafy.runit.domain.group.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.group.dto.response.GetGroupUsersInfo;
import com.ssafy.runit.domain.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController implements GroupDocs {

    private final GroupService groupService;

    @Override
    @GetMapping("/users")
    public RunItApiResponse<GetGroupUsersInfo> GetGroupUsersInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long groupId) {
        return RunItApiResponse.create(groupService.findUsersByGroupWithRank(groupId, userDetails.getUsername()), "그룹 구성원 정보 조회에 성공했습니다.");
    }
}
