package com.ssafy.runit.domain.track.controller;

import com.ssafy.runit.RunItApiResponse;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.track.dto.response.TrackImgResponse;
import com.ssafy.runit.domain.track.dto.response.TrackRouteResponse;
import com.ssafy.runit.domain.track.service.TrackService;
import com.ssafy.runit.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor

public class TrackController implements TrackDocs {
    private final UserRepository userRepository;
    private final TrackService trackService;

    @Override
    @GetMapping()
    public RunItApiResponse<TrackImgResponse> trackFindImg(Long recordId) {
        TrackImgResponse image = trackService.getTrackImg(recordId);
        return new RunItApiResponse<>(image, "성공");
    }

    @Override
    public RunItApiResponse<TrackRouteResponse> trackFindRoute(Long recordId) {
        TrackRouteResponse route = trackService.getTrackRoute(recordId);
        return new RunItApiResponse<>(route, "성공");
    }
}
