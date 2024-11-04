package com.ssafy.runit.domain.track.dto;

import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.track.entity.Track;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrackSaveDto {
    private Long id;
    private String url;
    private String path;

    public Track toEntity(Record record){
        return Track.builder()
                .id(record.getId())
                .trackImageUrl(url)
                .path(path)
                .build();
    }
}
