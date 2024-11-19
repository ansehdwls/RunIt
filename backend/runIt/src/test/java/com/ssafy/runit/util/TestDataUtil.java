package com.ssafy.runit.util;

import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.league.entity.League;
import com.ssafy.runit.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Component
public class TestDataUtil {

    public List<Group> createTestGroups(League league, int groupCount) {
        List<Group> groups = new ArrayList<>();
        for (int i = 1; i <= groupCount; i++) {
            Group group = Group
                    .builder()
                    .groupLeague(league)
                    .users(new HashSet<>())
                    .build();
            groups.add(group);
            league.getGroups().add(group);
        }
        return groups;
    }

    public List<User> createTestUsers(Group group, int userCount) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= userCount; i++) {
            User user = User.builder()
                    .userNumber("User_Number_" + group.getId() + "_" + i)
                    .userName("User_Name_" + group.getId() + "_" + i)
                    .imageUrl("image_" + group.getId() + "_" + i)
                    .experiences(new HashSet<>())
                    .fcmToken("fcmToken_" + group.getId() + "_" + i)
                    .userGroup(group)
                    .build();
            group.addUser(user);
            users.add(user);
        }
        return users;
    }

    public List<Experience> createTestExperiences(User user, int userCount) {
        List<Experience> experiences = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= userCount; i++) {
            long randomExperience = random.nextLong(100);
            Experience experience = Experience.builder()
                    .user(user)
                    .changed(randomExperience)
                    .createAt(LocalDateTime.now())
                    .build();
            experiences.add(experience);
            user.addExperience(experience);
        }
        return experiences;
    }
}
