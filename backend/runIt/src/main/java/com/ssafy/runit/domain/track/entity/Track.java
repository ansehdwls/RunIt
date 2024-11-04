package com.ssafy.runit.domain.track.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.runit.domain.record.entity.Record;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private long id;

    @OneToOne
    private Record record;

    private String trackImageUrl;

    private String path;

}
