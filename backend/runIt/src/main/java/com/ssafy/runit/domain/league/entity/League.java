package com.ssafy.runit.domain.league.entity;

import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.rank.LeagueRank;
import com.ssafy.runit.domain.rank.LeagueRankConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "league")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String leagueName;


    @OneToMany(mappedBy = "groupLeague")
    private Set<Group> groups;

    public void updateGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    @Convert(converter = LeagueRankConverter.class)
    @Column(name = "league_rank")
    @NotNull
    private LeagueRank rank;
}
