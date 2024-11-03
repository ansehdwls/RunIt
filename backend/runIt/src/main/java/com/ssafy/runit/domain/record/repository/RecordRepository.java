package com.ssafy.runit.domain.record.repository;

import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository {
    RecordGetResponse recordFindByUserId(Long userId);
    List<RecordGetResponse> recordListFindByUserId(Long userId);

}
