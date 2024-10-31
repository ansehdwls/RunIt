package com.ssafy.runit.domain.attendance.entity;

import com.ssafy.runit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Table(name = "attendance")
@Entity
@Builder
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp()
    private LocalDate createdAt;
}
