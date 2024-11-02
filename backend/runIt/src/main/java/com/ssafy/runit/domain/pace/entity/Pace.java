package com.ssafy.runit.domain.pace.entity;

import com.ssafy.runit.domain.record.entity.Record;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Pace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pace_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "record_id")
    private Record record;

    private LocalDateTime duration;
}
