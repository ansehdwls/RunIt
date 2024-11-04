package com.ssafy.runit.domain.track.repository;

import com.ssafy.runit.domain.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {

}
