package com.ssafy.runit.domain.split.repository;

import com.ssafy.runit.domain.split.entity.Split;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SplitRepository extends JpaRepository<Split, Long> {
    List<Split> findByRecordId(Long recordId);
}
