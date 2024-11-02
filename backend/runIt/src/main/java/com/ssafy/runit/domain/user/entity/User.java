package com.ssafy.runit.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.group.entity.Group;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

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
    private String userNumber;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonManagedReference
    private Group userGroup;

    public void updateGroup(Group group) {
        if (this.userGroup != null) {
            this.userGroup.getUsers().remove(this);
        }
        this.userGroup = group;
        group.addUser(this);
    }

    @OneToMany(mappedBy = "user")
    private Set<Experience> experiences;

    public void addExperience(Experience experience) {
        experiences.add(experience);
    }
}
