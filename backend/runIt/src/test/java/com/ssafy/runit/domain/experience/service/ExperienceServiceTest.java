package com.ssafy.runit.domain.experience.service;

import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ExperienceServiceTest {
    private static final Logger log = LoggerFactory.getLogger(ExperienceServiceTest.class);
    @Autowired
    EntityManager em;

    @Autowired
    ExperienceService experienceService;

    @Test
    public void 획득_경험치_저장() throws Exception{

        User user1 = new User();
        user1.setNickName("cheo");
        em.persist(user1);

        Experience ex1 = new Experience();
        ex1.setUser(user1);
        ex1.setChanged(100);
        ex1.setCreateAt(Timestamp.valueOf("2024-10-24 00:00:00"));

        System.out.println("ex1.getId() = " + ex1.getId());
        System.out.println("ex1.getId() = " + experienceService.experienceSave(ex1));


//        assertEquals(ex1.getId(), experienceService.experienceSave(ex1));
    }

    @Test
    public void 경험치_획득() throws Exception{

        User user1 = new User();
        user1.setNickName("cheo");
        em.persist(user1);


        Experience ex1 = new Experience();
        ex1.setUser(user1);
        ex1.setChanged(100);
        ex1.setCreateAt(Timestamp.valueOf("2024-10-24 00:00:00"));

        Experience ex2 = new Experience();
        ex2.setUser(user1);
        ex2.setChanged(200);
        ex2.setCreateAt(Timestamp.valueOf("2024-10-20 00:00:00"));

        Experience ex3 = new Experience();
        ex3.setUser(user1);
        ex3.setChanged(300);
        ex3.setCreateAt(Timestamp.valueOf("2024-10-23 00:00:00"));

        em.persist(ex1);

        em.persist(ex2);

        em.persist(ex3);

        assertEquals( experienceService.experienceChangedSum(user1.getId()), ex1.getChanged() + ex2.getChanged() +ex3.getChanged());

    }
}