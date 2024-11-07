package com.ssafy.runit.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.domain.user.dto.response.UserInfoResponse;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.GroupErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(readOnly = true)
    public User findByUserNumber(String userNumber) {
        return userRepository.findByUserNumber(userNumber).orElseThrow(
                () -> new CustomException(AuthErrorCode.UNREGISTERED_USER_ERROR)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllFcmTokens() {
        return userRepository.findAllFcmTokens();
    }

    @Override
    @Transactional
    public void saveFcmToken(String userNumber, String fcmToken) {
        User user = findByUserNumber(userNumber);
        userRepository.updateFcmTokenByUserId(user.getId(), fcmToken);
    }

    @Override
    public List<UserInfoResponse> findUserByGroup(long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND_ERROR)
        );
        String cacheKey = "group:" + groupId;
        List<Object> rawUsers = redisTemplate.opsForList().range(cacheKey, 0, -1);
        List<UserInfoResponse> users = new ArrayList<>();
        if (rawUsers == null || rawUsers.isEmpty()) {
            log.debug("redis 저장!");
            users = userRepository.findUserByUserGroup(group)
                    .stream()
                    .map(UserInfoResponse::fromEntity)
                    .toList();
            redisTemplate.opsForList().rightPushAll(cacheKey, users); // List로 저장
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            for (Object userObj : rawUsers) {
                UserInfoResponse userInfoResponse = objectMapper.convertValue(userObj, UserInfoResponse.class);
                users.add(userInfoResponse);
            }
            log.debug("redis 조회 : {}", users.size());
        }

        return users;
    }
}
