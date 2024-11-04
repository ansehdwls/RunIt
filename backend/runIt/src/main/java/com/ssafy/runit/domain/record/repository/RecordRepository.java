package com.ssafy.runit.domain.record.repository;


import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Optional<RecordGetResponse> findFirstByUserId(Long userId);
    List<RecordGetResponse> findByUserId(Long userId);

}
