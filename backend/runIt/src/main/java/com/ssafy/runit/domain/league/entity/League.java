package com.ssafy.runit.domain.league.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssafy.runit.domain.group.entity.Group;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "league")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String leagueName;


    @OneToMany(mappedBy = "groupLeague")
    private Set<Group> groups;
}
