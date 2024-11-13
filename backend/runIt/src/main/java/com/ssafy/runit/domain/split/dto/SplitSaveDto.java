package com.ssafy.runit.domain.split.dto;

import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.split.entity.Split;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SplitSaveDto {

    private Integer pace;
    private Integer bpm;

    public Split toEntity(Record record) {
        return Split.builder()
                .record(record)
                .pace(pace)
                .bpm(bpm)
                .build();
    }
}
