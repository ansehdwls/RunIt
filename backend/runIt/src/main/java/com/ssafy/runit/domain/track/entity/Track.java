package com.ssafy.runit.domain.track.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.runit.domain.record.entity.Record;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "record_id")
    private Record record;

    private String trackImageUrl;

    private String path;

}
