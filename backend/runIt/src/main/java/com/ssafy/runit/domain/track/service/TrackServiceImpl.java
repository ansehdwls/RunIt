package com.ssafy.runit.domain.track.service;

import com.ssafy.runit.domain.track.dto.response.TrackImgResponse;
import com.ssafy.runit.domain.track.dto.response.TrackRouteResponse;
import com.ssafy.runit.domain.track.repository.TrackRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.TrackErrorCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.N;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;

    @Override
    public TrackImgResponse getTrackImg(Long recordId) {
        return trackRepository.findTrackImageUrlByRecordId(recordId).orElseThrow(
                () -> new CustomException(TrackErrorCode.NOT_FOUND_TRACK_IMG)
        );
    }

    @Override
    public TrackRouteResponse getTrackRoute(Long recordId) {
        return trackRepository.findTrackPathByRecordId(recordId).orElseThrow(
                () -> new CustomException(TrackErrorCode.NOT_FOUND_TRACK_ROUTE)
        );
    }
}
