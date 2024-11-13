package com.ssafy.runit.domain.record.entity;

import com.ssafy.runit.domain.split.entity.Split;
import com.ssafy.runit.domain.track.entity.Track;
import com.ssafy.runit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "record")
    private Track track;

    @OneToMany(mappedBy = "record")
    private List<Split> splitList;

    private Double distance;

    private Integer bpm;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isPractice;
}
