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

    private Double distance;

    private Integer bpm;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isPractice;

    public Double getPace() {
        if (startTime == null || endTime == null || distance < 0) {
            return null;
        }
        Duration duration = Duration.between(startTime, endTime);
        long minutes = duration.toMinutes();
        long seconds = duration.toSeconds() % 60;
        double totalMinutes = minutes + (seconds / 60.0);
        if (totalMinutes <= 0) {
            return null;
        }
        double pace = totalMinutes / distance;
        System.out.println("record pace : " + pace + " total minutes : " + totalMinutes);
        pace = Math.round(pace * 100) / 100.0;
        return pace;
    }

    public String getPaceFormatted() {
        Double pace = getPace();
        if (pace == null) {
            return "N/A";
        }
        int minutes = pace.intValue();
        int seconds = (int) Math.round((pace - minutes) * 60);
        return String.format("%d:%02d min/km", minutes, seconds);
    }
}
