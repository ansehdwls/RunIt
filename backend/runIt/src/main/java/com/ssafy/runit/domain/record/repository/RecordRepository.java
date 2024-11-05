package com.ssafy.runit.domain.record.repository;


import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("SELECT r FROM Record r WHERE r.user.id IN :userId AND r.id IN :recordId")
    Record findByUserIdAndRecordId(@Param("userId") Long userId, @Param("recordId") Long recordId);

    List<RecordGetResponse> findByUserId(Long userId);

}
