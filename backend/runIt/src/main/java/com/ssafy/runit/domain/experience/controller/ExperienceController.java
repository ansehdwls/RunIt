package com.ssafy.runit.domain.experience.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.experience.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ExperienceController implements ExperienceDocs{

    private final ExperienceService experienceService;

    @Override
    @PostMapping("/exp")
    public RunItApiResponse<Void> saveExperience(@RequestBody ExperienceSaveRequest experienceSaveRequest) {
        log.debug("user = {}", experienceSaveRequest.getUserId());
        experienceService.experienceSave(experienceSaveRequest.getUserId(), experienceSaveRequest);
        return null;
    }

    @Override
    @GetMapping("/exp")
    public RunItApiResponse<List<Experience>> getListExperience(@RequestParam("userId") Long userId) {
        log.debug("getListExperience = {}", userId);
        List<Experience> experienceList = experienceService.experienceList(userId);
        log.debug("getListExperience = {}", experienceList.get(0).getActivity());
        return new RunItApiResponse<>(experienceList, "성공");
    }
}
