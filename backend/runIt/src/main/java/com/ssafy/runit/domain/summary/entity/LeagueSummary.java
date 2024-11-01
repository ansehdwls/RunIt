package com.ssafy.runit.domain.summary.entity;

import com.ssafy.runit.domain.league.entity.League;
import com.ssafy.runit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LeagueSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

    @ManyToMany
    @JoinTable(
            name = "league_summary_advanced_users",
            joinColumns = @JoinColumn(name = "league_summary_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> advancedUsers;

    @ManyToMany
    @JoinTable(
            name = "league_summary_degraded_users",
            joinColumns = @JoinColumn(name = "league_summary_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> deGradeUsers;

    @ManyToMany
    @JoinTable(
            name = "league_summary_wait_users",
            joinColumns = @JoinColumn(name = "league_summary_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> waitUsers;
}
