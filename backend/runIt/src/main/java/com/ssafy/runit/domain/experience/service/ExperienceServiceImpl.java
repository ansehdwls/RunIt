package com.ssafy.runit.domain.experience.service;


import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.dto.response.ExperienceGetListResponse;
import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.experience.repository.ExperienceRepository;
import com.ssafy.runit.domain.rank.service.RankService;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.domain.user.service.UserService;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.ExperienceErrorCode;
import com.ssafy.runit.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final RankService rankService;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public void experienceSave(UserDetails userDetails, ExperienceSaveRequest request) {
//      기존의 user id 받아서 처리하는 부분
        User user = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();
        Experience exp = request.Mapper(user, LocalDateTime.now());
        rankService.updateScore(user.getUserGroup().getId(), String.valueOf(user.getId()), request.getChanged());
        experienceRepository.save(exp);
    }

    public Long experienceChangedSum(Long id) {
        LocalDate lastMonday = DateUtils.getLastMonday();
        return experienceRepository.experienceChangedSum(id, lastMonday.atStartOfDay());
    }

    @Override
    public List<ExperienceGetListResponse> experienceList(Long userId) {
        List<ExperienceGetListResponse> expList = experienceRepository.findByUser_Id(userId);
        return expList;
    }

    @Override
    public Long experienceGetToday(UserDetails userDetails) {
        User user = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow(
                ()-> new CustomException(AuthErrorCode.UNREGISTERED_USER_ERROR)
        );

        LocalDate today = LocalDate.now();

        LocalDateTime startDay = today.atStartOfDay();
        LocalDateTime endDay = today.plusDays(1).atStartOfDay();

        return experienceRepository.findByUserIdAndCreateAtBetween(user.getId(), startDay, endDay)
                .stream()
                .filter(experience -> experience.getActivity().equals("거리"))
                .mapToLong(Experience :: getChanged)
                .sum();

    }
}
