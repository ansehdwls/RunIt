package com.ssafy.runit.domain.record.entity;

import com.ssafy.runit.domain.split.entity.Split;
import com.ssafy.runit.domain.track.entity.Track;
import com.ssafy.runit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
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

    public void updateSplitList(List<Split> splitList) {
        this.splitList = splitList;
    }

    private Double distance;

    private Integer bpm;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isPractice;

    private Integer duration;

    public Double getPace() {
        if (startTime == null || endTime == null || distance < 0 || duration < 0) {
            return null;
        }
        long minutes = duration / 60; //21
        long seconds = duration % 60; //40
        double totalMinutes = minutes + (seconds / 60.0);
        if (totalMinutes <= 0) {
            return null;
        }
        double pace = totalMinutes / distance;
        pace = Math.round(pace * 100) / 100.0;
        return pace;
    }
}
