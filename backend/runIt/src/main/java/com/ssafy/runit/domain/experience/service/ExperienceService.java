package com.ssafy.runit.domain.experience.service;

import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.entity.Experience;

public interface ExperienceService {
    void experienceSave(Long userId, ExperienceSaveRequest request);

    Long experienceChangedSum(Long id);
}
