package com.ssafy.runit.util;


import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExperienceUtil {

    public static Map<Long, Long> getGroupUserExperience(List<Long> userIds, List<Long[]> experienceSums) {
        return experienceSums.stream().collect(
                Collectors.toMap(
                        row -> row[0],
                        row -> row[1]
                )
        );
    }

    public static List<Pair<String, Long>> experienceCalc(boolean today, int size, double dis){

        List<Pair<String, Long>> result = new ArrayList<>();

        if (!today){
            result.add(Pair.of("출석", 10L));

            if (size == 4){
                result.add(Pair.of("출석 보너스", 50L));
            }
        }

        result.add(Pair.of("거리", (long) (dis* 1000) / 100));

        return result;
    }
}
