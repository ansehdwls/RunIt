package com.ssafy.runit.domain.track.dto.response;

import lombok.Builder;

@Builder
public record TrackRouteResponse(
        String path
) {
    public static TrackRouteResponse fromEntity(String path){
        return TrackRouteResponse.builder()
                .path(path)
                .build();
    }
}
