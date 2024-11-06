package com.ssafy.runit.domain.record.dto.response;

import java.time.LocalDateTime;

public record RecordGetListResponse(
        long id,
        double distance,
        Integer bpm,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
