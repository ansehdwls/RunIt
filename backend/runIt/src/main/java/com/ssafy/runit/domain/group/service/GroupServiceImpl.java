package com.ssafy.runit.domain.group.service;

import com.ssafy.runit.domain.experience.repository.ExperienceRepository;
import com.ssafy.runit.domain.group.dto.response.GetGroupUsersInfo;
import com.ssafy.runit.domain.group.dto.response.GroupUserInfo;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.GroupErrorCode;
import com.ssafy.runit.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final ExperienceRepository experienceRepository;

    @Override
    @Transactional(readOnly = true)
    public GetGroupUsersInfo findUsersByGroupWithRank(Long groupId, String userNumber) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND_ERROR)
        );
        User user = group.getUsers().stream()
                .filter(u -> u.getUserNumber().equals(userNumber))
                .findFirst()
                .orElseThrow(() -> new CustomException(GroupErrorCode.GROUP_NO_USERS_ERROR));
        List<GroupUserInfo> userInfos = findUsersByGroup(group);
        return new GetGroupUsersInfo().Mapper(userInfos, user, group.getGroupLeague().getRank().getRank());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupUserInfo> findUsersByGroup(Group group) {
        LocalDate lastMonday = DateUtils.getLastMonday();
        List<Long> userIds = group.getUsers().stream().map(User::getId).toList();
        return experienceRepository.sumExperienceByUserIdsAndStartDate(userIds, lastMonday.atStartOfDay());
    }
}
