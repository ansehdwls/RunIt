package com.ssafy.runit.domain.experience.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Temporal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExperienceRepository {
    private final EntityManager em;

    public void save(Experience experience){
        em.persist(experience);
    }

    public Long getUserExperience(Long id){
        String sql = "select Sum(e.changed) from Experience e where e.user.id = :userId";


        return em.createQuery(sql, Long.class)
                .setParameter("userId", id)
                .getSingleResult();
    }

    public List<Experience> findWeekAll(Long id, LocalDate date){

        LocalDate localDate = getStartOfWeek(date);

        String sql = "select e from experience e where e.user.id = :userId AND e.startDate BETWEEN :date AND :localDate";

        return em.createQuery(sql, Experience.class)
                .setParameter("userId", id)
                .setParameter("date", date)
                .setParameter("localDate", localDate)
                .getResultList();

    }


    public LocalDate getStartOfWeek(LocalDate date){
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

}
