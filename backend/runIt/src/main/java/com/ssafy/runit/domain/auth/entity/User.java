package com.ssafy.runit.domain.auth.entity;

import com.ssafy.runit.domain.group.entity.Group;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
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
    private Group userGroup;
}
