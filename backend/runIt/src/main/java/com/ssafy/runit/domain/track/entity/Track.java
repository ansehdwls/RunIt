package com.ssafy.runit.domain.track.entity;

import com.ssafy.runit.domain.record.entity.Record;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private long id;

    private String trackImageUrl;

    private String path;

}
