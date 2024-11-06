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

    private LocalDateTime duration;
    private double bmp;

    public Pace toEntity(Record record){
        return Pace.builder()
                .record(record)
                .duration(duration)
                .bpm(bmp)
                .build();
    }
}
