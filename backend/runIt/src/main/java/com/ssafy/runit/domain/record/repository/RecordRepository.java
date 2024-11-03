package com.ssafy.runit.domain.record.repository;

import com.ssafy.runit.domain.record.dto.response.recordGetResponse;
import com.ssafy.runit.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository {
    recordGetResponse recordFindByUserId(Long userId);
    List<recordGetResponse> recordListFindByUserId(Long userId);

}
