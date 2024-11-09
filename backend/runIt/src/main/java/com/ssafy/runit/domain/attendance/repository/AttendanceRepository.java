package com.ssafy.runit.domain.attendance.repository;

import com.ssafy.runit.domain.attendance.entity.Attendance;
import com.ssafy.runit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByUserAndCreatedAtAfterOrderByCreatedAtAsc(User user, LocalDate date);


    @Query(value = "SELECT * FROM Attendance WHERE DATE(created_at) = DATE(:date)", nativeQuery = true)
Optional<Attendance> findByCreatedAt(@Param("date") LocalDate date);
}
