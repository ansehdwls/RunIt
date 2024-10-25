package com.ssafy.runit.domain.auth.service;

import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.auth.entity.User;
import com.ssafy.runit.domain.auth.repository.UserRepository;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.group.repository.GroupRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.GroupErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;


    @Override
    public void registerUser(UserRegisterRequest request) {
        if (!request.isValid()) {
            log.error("잘못된 입력 형식 : email:{} name : {} image:{}", request.getUserEmail(), request.getUserName(), request.getUserImageUrl());
            throw new CustomException(AuthErrorCode.INVALID_DATA_FORM);
        }

        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            log.error("가입된 사용자 : email : {}", request.getUserEmail());
            throw new CustomException(AuthErrorCode.DUPLICATED_USER_ERROR);
        }
        Group group = groupRepository.findDefaultGroup().orElseThrow(
                () -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND_ERROR)
        );
        log.debug("group : {}", group);
        User user = request.Mapper(group);
        log.debug("user : {}", user);
        userRepository.save(user);
    }
}
