package com.ssafy.runit.domain.user.service;

import com.ssafy.runit.domain.user.entity.User;

import java.util.List;

public interface UserService {

    User findByEmail(String userEmail);

    List<String> findAllFcmTokens();

    void saveFcmToken(String userEmail, String fcmToken);
}
