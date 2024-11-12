package com.ssafy.runit.domain.experience.service;

import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.dto.response.ExperienceGetListResponse;
import com.ssafy.runit.domain.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ExperienceService {
    void experienceSave(UserDetails userDetails, ExperienceSaveRequest request);

    Long experienceChangedSum(Long userId);

    List<ExperienceGetListResponse> experienceList(Long userId);

    Long experienceGetToday(UserDetails userDetails);
}
