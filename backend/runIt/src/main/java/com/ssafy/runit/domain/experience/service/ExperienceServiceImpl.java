package com.ssafy.runit.domain.experience.service;


import com.ssafy.runit.domain.auth.entity.User;
import com.ssafy.runit.domain.auth.repository.UserRepository;
import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.experience.repository.ExperienceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {
    private final ExperienceRepository experienceRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void experienceSave(Long userId,ExperienceSaveRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Experience exp = request.Mapper(user);
        experienceRepository.save(exp);
    }

    public Long experienceChangedSum(Long id){

        return id;
    }



}
