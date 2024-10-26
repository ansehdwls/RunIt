package com.ssafy.runit.domain.auth.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssafy.runit.domain.group.entity.Group;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String imageUrl;
    @Column
    private String fcmToken;
    @Column(nullable = false)
    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonManagedReference
    private Group userGroup;
}
