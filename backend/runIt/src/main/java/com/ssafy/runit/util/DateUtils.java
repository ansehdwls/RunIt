package com.ssafy.runit.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {

    public static LocalDateTime getLastMonday() {
        LocalDate today = LocalDate.now();
        LocalDate lastMonday = today.with(DayOfWeek.MONDAY);
        return lastMonday.atStartOfDay();
    }
}
