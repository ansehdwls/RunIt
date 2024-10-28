package com.ssafy.runit.domain.experience.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.experience.service.ExperienceService;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ExperienceController implements ExperienceDocs{

    private final ExperienceService experienceService;

    private final UserRepository userRepository;

    @Override
    @PostMapping("/exp")
    public RunItApiResponse<Void> saveExperience(@RequestBody ExperienceSaveRequest experienceSaveRequest) {
        experienceService.experienceSave(experienceSaveRequest.getUserId(), experienceSaveRequest);
        return null;
    }

    @Override
    @GetMapping("/exp")
    public RunItApiResponse<List<Experience>> getListExperience(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow();

        List<Experience> experienceList = experienceService.experienceList(user.getId());
        return new RunItApiResponse<>(experienceList, "성공");
    }

    @Override
    @GetMapping("/week/exp")
    public RunItApiResponse<Long> getWeekSumExperience(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow();

        return new RunItApiResponse<>(experienceService.experienceChangedSum(user.getId()), "성공");
    }


}
