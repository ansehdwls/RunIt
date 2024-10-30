package com.ssafy.runit.domain.experience.service;


import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.dto.response.ExperienceGetListResponse;
import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.experience.repository.ExperienceRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final EntityManager em;

    @Override
    @Transactional
    public void experienceSave(Long userId, ExperienceSaveRequest request) {
//      기존의 user id 받아서 처리하는 부분
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(AuthErrorCode.UNREGISTERED_USER_ERROR));
        Experience exp = request.Mapper(user);
        experienceRepository.save(exp);
    }

    public Long experienceChangedSum(Long id) {

        LocalDate today = LocalDate.now();
        LocalDate monday = getStartOfWeek(today);

        Timestamp convertToday = Timestamp.valueOf(today.atTime(LocalTime.now()));

        Timestamp convertMonday = Timestamp.valueOf(monday.atTime(LocalTime.MIN));

        String jpql = "SELECT SUM(e.changed) \n" +
                "FROM Experience e \n" +
                "WHERE e.user.id = :id \n" +
                "AND e.createAt >= :startDate";

        Long result = em.createQuery(jpql, Long.class)
                .setParameter("id", id)
                .setParameter("startDate", convertMonday)
                .getSingleResult();
        return result;
    }

    public static LocalDate getStartOfWeek(LocalDate date) {
        return date.with(DayOfWeek.MONDAY); // 해당 주의 월요일 날짜 반환
    }

    @Override
    public List<ExperienceGetListResponse> experienceList(Long userId) {
        List<ExperienceGetListResponse> expList = experienceRepository.findByUser_Id(userId);
        return expList;
    }
}
