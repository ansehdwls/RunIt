package com.ssafy.runit.util;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {

    public static LocalDate getLastMonday() {
        LocalDate today = LocalDate.now();
        return today.getDayOfWeek() == DayOfWeek.MONDAY ? today : today.with(DayOfWeek.MONDAY);
    }

    public static LocalDate getLastMonday(LocalDate today) {
        return today.with(DayOfWeek.MONDAY);
    }

    public static LocalDate getLastSunday(LocalDate today) {
        return today.with(DayOfWeek.SUNDAY);
    }

    public static String getDayNameInKorean(DayOfWeek day) {
        return switch (day) {
            case SUNDAY -> "일요일";
            case MONDAY -> "월요일";
            case TUESDAY -> "화요일";
            case WEDNESDAY -> "수요일";
            case THURSDAY -> "목요일";
            case FRIDAY -> "금요일";
            case SATURDAY -> "토요일";
        };
    }


    public static long computeTTLForNextWeek() {
        LocalDate expirationDate = getLastMonday().plusDays(7);
        return Duration.between(LocalDate.now().atStartOfDay(), expirationDate.atStartOfDay()).toSeconds();
    }

    public static Duration computeDurationForNextWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationDate = getLastMonday().plusDays(7).atStartOfDay();
        return Duration.between(now, expirationDate);
    }

    public static Long getSpendTime(LocalDateTime stTime, LocalDateTime endTime) {
        Duration duration = Duration.between(stTime, endTime);

        long hours = duration.toHours(); // 총 시간 차이
        long minutes = duration.toMinutes() % 60; //

        return  (hours * 60) + minutes;

    }
}
