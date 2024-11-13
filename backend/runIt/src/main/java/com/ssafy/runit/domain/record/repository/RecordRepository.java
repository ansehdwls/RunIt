package com.ssafy.runit.domain.record.repository;


import com.ssafy.runit.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("SELECT r FROM Record r WHERE r.user.id IN :userId AND r.id IN :recordId")
    Optional<Record> findByUserIdAndRecordId(@Param("userId") Long userId, @Param("recordId") Long recordId);

    List<Record> findByUserId(Long userId);


    List<Record> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Modifying
    @Query("UPDATE Record r SET r.isPractice = :isPractice WHERE r.id = :recordId")
    int updateRecordPractice(@Param("recordId") Long recordId, @Param("isPractice") Boolean isPractice);
}
