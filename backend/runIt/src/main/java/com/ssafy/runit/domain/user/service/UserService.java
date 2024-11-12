package com.ssafy.runit.domain.user.service;

import com.ssafy.runit.domain.user.entity.User;

import java.util.List;

public interface UserService {

    User findByUserNumber(String userNumber);

    List<String> findAllFcmTokens();

    void saveFcmToken(String userNumber, String fcmToken);
}
