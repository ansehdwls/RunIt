package com.ssafy.runit.domain.track.service;

import com.ssafy.runit.domain.track.dto.response.TrackImgResponse;
import com.ssafy.runit.domain.track.dto.response.TrackRouteResponse;

public interface TrackService {
    TrackImgResponse getTrackImg(Long recordId);

    TrackRouteResponse getTrackRoute(Long recordId);
}
