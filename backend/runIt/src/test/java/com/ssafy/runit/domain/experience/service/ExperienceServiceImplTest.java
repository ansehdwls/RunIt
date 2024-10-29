package com.ssafy.runit.domain.experience.service;


import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.experience.repository.ExperienceRepository;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceServiceImplTest {

    @InjectMocks
    private ExperienceServiceImpl experienceService;

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private UserRepository userRepository;

    private User testUser;
    private ExperienceSaveRequest request;

    @BeforeEach
    public void setUp(){

        testUser = new User();

        testUser = User.builder()
                .id(1L)
                .userName("테스트 유저")
                .imageUrl("https://example.com/image.png")
                .userEmail("testuser@example.com")
                .fcmToken("test_fcm_token")
                .build();

        request = new ExperienceSaveRequest();

        request = ExperienceSaveRequest.builder()
                        .userId(1L)
                        .activity("코딩 연습")
                        .changed(50)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .startDate(new Date(System.currentTimeMillis()))
                        .build();
    }


    @Test
    public void testExperienceSave() {
        // UserRepository의 findById 동작 모의 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // experienceRepository의 save 메서드 동작 모의 설정 (필요에 따라)
        // when(experienceRepository.save(any(Experience.class))).thenReturn(...);

        // 테스트 대상 메서드 호출
        experienceService.experienceSave(1L, request);

        // 검증
        verify(userRepository, times(1)).findById(1L);
        verify(experienceRepository, times(1)).save(any(Experience.class));
    }



}