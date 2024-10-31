package com.ssafy.runit.domain.attendance.repository;

import com.ssafy.runit.domain.attendance.entity.Attendance;
import com.ssafy.runit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByUserAndCreatedAtAfterOrderByCreatedAtAsc(User user, LocalDate date);
}
