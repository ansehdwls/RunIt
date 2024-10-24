package com.ssafy.runit.domain.group.repository;

import com.ssafy.runit.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("SELECT g FROM Group g WHERE g.id = 1")
    Optional<Group> findDefaultGroup();
}
