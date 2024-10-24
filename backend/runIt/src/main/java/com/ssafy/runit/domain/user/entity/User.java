package com.ssafy.runit.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private long id;

//    private Group group;

    private String nickName;

    private String profileImg;

    // Bcrypt
    private String fcmToken;

}
