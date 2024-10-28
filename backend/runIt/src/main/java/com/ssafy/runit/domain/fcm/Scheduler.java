package com.ssafy.runit.domain.fcm;

import com.ssafy.runit.domain.fcm.dto.SendFcmMessage;
import com.ssafy.runit.domain.fcm.service.FcmService;
import com.ssafy.runit.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class Scheduler {

    private final FcmService fcmService;
    private final UserService userService;

    @Scheduled(cron = "${schedule.week-start.cron}")
    public void sendWeekStartNotification() {
        List<String> tokens = userService.findAllFcmTokens();
        String title = SchedulerConstants.WEEK_START_NOTIFICATION[0];
        String body = SchedulerConstants.WEEK_START_NOTIFICATION[1];
        String link = SchedulerConstants.WEEK_START_NOTIFICATION[2];
        String imageUrl = SchedulerConstants.WEEK_START_NOTIFICATION[3];

        for (String token : tokens) {
            fcmService.sendNotification(
                    SendFcmMessage.builder()
                            .token(token)
                            .link(link)
                            .image(imageUrl)
                            .body(body)
                            .title(title).build());
        }
    }
}