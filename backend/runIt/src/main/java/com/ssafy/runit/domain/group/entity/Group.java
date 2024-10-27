package com.ssafy.runit.domain.group.entity;

import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.league.entity.League;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "`group`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "league_id", nullable = false)
    private League groupLeague;


    @OneToMany(mappedBy = "userGroup")
    private Set<User> users;
}
