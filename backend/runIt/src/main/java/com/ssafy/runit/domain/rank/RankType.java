package com.ssafy.runit.domain.rank;

public enum RankType {
    EXPERIENCE, PACE, DISTANCE, NONE;

    public static RankType fromString(String rankTypeStr) {
        return switch (rankTypeStr) {
            case "experience" -> EXPERIENCE;
            case "pace" -> PACE;
            case "distance" -> DISTANCE;
            default -> NONE;
        };
    }
}
