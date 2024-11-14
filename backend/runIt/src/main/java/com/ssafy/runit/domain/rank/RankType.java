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

    public static String calRankScore(RankType type, double score) {
        return switch (type) {
            case PACE -> {
                score = Math.round(score * 100) / 100.0;
                yield PACE.getPaceFormatted(score);
            }
            case DISTANCE -> String.format("%.2f km", score);
            default -> (int) score + "xp";
        };
    }

    public String getPaceFormatted(double pace) {
        if (pace == 0) {
            return "--";
        }
        int minutes = (int) pace;
        int seconds = (int) Math.round((pace - minutes) * 60);
        return String.format("%d'%02d'/km", minutes, seconds);
    }
}
