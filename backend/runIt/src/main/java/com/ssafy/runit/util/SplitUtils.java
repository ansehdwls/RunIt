package com.ssafy.runit.util;

import com.ssafy.runit.domain.split.entity.Split;

import java.util.List;

public class SplitUtils {
    public static double getPaceAvg(List<Split> splitList){
        double result = 0.0;

        if (!splitList.isEmpty()) {
            result = splitList.stream()
                    .mapToDouble(Split::getPace) // 각 요소에서 특정 속성 값을 추출하여 더함
                    .sum() / splitList.size();
        }

        return DoubleUtils.distanceFormatter(result);
    }
}
