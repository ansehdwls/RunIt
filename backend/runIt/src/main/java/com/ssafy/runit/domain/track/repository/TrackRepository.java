package com.ssafy.runit.domain.track.repository;

import com.ssafy.runit.domain.track.dto.response.TrackImgResponse;
import com.ssafy.runit.domain.track.dto.response.TrackRouteResponse;
import com.ssafy.runit.domain.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {
    Optional<TrackImgResponse> findTrackImageUrlByRecordId(Long recordId);

    Optional<TrackRouteResponse> findTrackPathByRecordId(Long recordId);
}
