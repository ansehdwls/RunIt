package com.ssafy.runit.domain.record.entity;

import com.ssafy.runit.domain.pace.entity.Pace;
import com.ssafy.runit.domain.track.entity.Track;
import com.ssafy.runit.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
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
     @Column(name = "record_id")
     private long id;

     @ManyToOne
     @JoinColumn(name = "user_id")
     private User user;

     @OneToOne(mappedBy = "record")
     private Track track;

     @OneToMany(mappedBy = "record")
     private List<Pace> paceList;

     private double distance;

     private Integer bpm;

     private LocalDateTime startTime;

     private LocalDateTime endTime;

}
