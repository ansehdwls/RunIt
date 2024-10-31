package com.ssafy.runit.domain.experience.service;

import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.dto.response.ExperienceGetListResponse;
import com.ssafy.runit.domain.user.entity.User;

import java.util.List;

public interface ExperienceService {
    void experienceSave(User user, ExperienceSaveRequest request);

    Long experienceChangedSum(Long userId);

    List<ExperienceGetListResponse> experienceList(Long userId);
}
