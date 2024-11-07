package com.ssafy.runit.domain.pace.dto;

import com.ssafy.runit.domain.pace.entity.Pace;
import com.ssafy.runit.domain.record.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaceSaveDto {

    private Integer pace;
    private Integer bpm;

    public Pace toEntity(Record record){
        return Pace.builder()
                .record(record)
                .pace(pace)
                .bpm(bpm)
                .build();
    }
}
