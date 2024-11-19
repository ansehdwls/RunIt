package com.ssafy.runit.domain.track.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.runit.domain.record.entity.Record;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "record_id")
    private Record record;

    private String trackImageUrl;
    @Lob
    private String path;

}
