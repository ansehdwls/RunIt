package com.ssafy.runit.domain.experience.service;

import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.dto.response.ExperienceGetListResponse;
import com.ssafy.runit.domain.experience.entity.Experience;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExperienceService {
    Long experienceSave(Long userId, ExperienceSaveRequest request);

    Long experienceChangedSum(Long userId);

    List<ExperienceGetListResponse> experienceList(Long userId);
}
