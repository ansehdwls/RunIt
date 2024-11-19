package com.ssafy.runit.domain.track.dto.response;

import lombok.Builder;

@Builder
public record TrackImgResponse(
    String trackImageUrl
) {
    public static TrackImgResponse fromEntity(String url){
        return TrackImgResponse.builder()
                .trackImageUrl(url)
                .build();
    }
}
