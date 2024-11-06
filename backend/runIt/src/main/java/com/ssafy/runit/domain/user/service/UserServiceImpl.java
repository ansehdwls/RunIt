package com.ssafy.runit.domain.user.service;

import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
}
