package com.ssafy.runit.domain.group.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssafy.runit.domain.auth.entity.User;
import com.ssafy.runit.domain.league.entity.League;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "`group`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "league_id", nullable = false)
    @JsonManagedReference
    private League groupLeague;


    @OneToMany(mappedBy = "userGroup")
    @JsonBackReference
    private Set<User> users;
}
