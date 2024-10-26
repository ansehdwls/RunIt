package com.ssafy.runit.domain.user.service;

import com.ssafy.runit.domain.user.entity.User;

public interface UserService {

    User findByEmail(String userEmail);
}
