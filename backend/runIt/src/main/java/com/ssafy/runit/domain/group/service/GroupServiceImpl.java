package com.ssafy.runit.domain.group.service;

import com.ssafy.runit.domain.experience.repository.ExperienceRepository;
import com.ssafy.runit.domain.group.dto.response.GetGroupUsersResponse;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.GroupErrorCode;
import com.ssafy.runit.util.DateUtils;
import com.ssafy.runit.util.ExperienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final ExperienceRepository experienceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GetGroupUsersResponse> findUsersByGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND_ERROR)
        );
        LocalDate lastMonday = DateUtils.getLastMonday();
        List<Long> userIds = group.getUsers().stream().map(User::getId).toList();
        List<Long[]> experienceSums = experienceRepository.sumExperienceByUserIdsAndStartDate(userIds, lastMonday.atStartOfDay());
        Map<Long, Long> map = ExperienceUtil.getGroupUserExperience(userIds, experienceSums);
        return group.getUsers()
                .stream()
                .map(user ->
                        GetGroupUsersResponse
                                .fromEntity(user, map.getOrDefault(user.getId(), 0L)))
                .toList();
    }
}
