package com.ssafy.runit.util;

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
}
