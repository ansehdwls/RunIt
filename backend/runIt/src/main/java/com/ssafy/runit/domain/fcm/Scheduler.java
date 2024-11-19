package com.ssafy.runit.domain.fcm;

import com.ssafy.runit.domain.fcm.dto.SendFcmMessage;
import com.ssafy.runit.domain.fcm.service.FcmService;
import com.ssafy.runit.domain.summary.service.LeagueSummaryService;
import com.ssafy.runit.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class Scheduler {

    private final FcmService fcmService;
    private final UserService userService;
    private final LeagueSummaryService leagueSummaryService;

    //@Scheduled(cron = "${schedule.week-start.cron}")
    @Scheduled(cron = "0 0 0 * * ?") // 임시 스케쥴링 매일 00시 자정ㅈ
    public void sendWeekStartNotification() {
        long startTime = System.currentTimeMillis();
        leagueSummaryService.processWeeklySummary();
        log.debug("스케쥴러 시작 !! : {}", LocalDate.now());
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
        long endTime = System.currentTimeMillis();
        log.debug("스케쥴러 끝 !! : {} 소요시간 : {}ms,", LocalDate.now(), endTime - startTime);
    }
}