package com.ssafy.runit.domain.experience.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
