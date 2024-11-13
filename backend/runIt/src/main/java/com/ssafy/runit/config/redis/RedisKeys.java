package com.ssafy.runit.config.redis;

public final class RedisKeys {

    public static final String EXPERIENCE_RANK_KEY = "exp_rank:%s";
    public static final String EXPERIENCE_PREVIOUS_RANK_KEY = "exp_previous_rank:%s";

    public static final String USER_PACE_KEY = "user_pace:%s";
    public static final String USER_PACE_SUB_KEY_TOTAL_PACE = "total_pace";
    public static final String USER_PACE_SUB_KEY_COUNT = "count";


    public static final String PACE_RANK_KEY = "pace_rank:%s";

    public static String getUserPaceKey(String userId) {
        return String.format(USER_PACE_KEY, userId);
    }

    public static String getPaceRankKey(String groupId) {
        return String.format(PACE_RANK_KEY, groupId);
    }

    public static String getExperienceRankKey(String groupId) {
        return String.format(EXPERIENCE_RANK_KEY, groupId);
    }

    public static String getExperiencePreviousRankKey(String groupId) {
        return String.format(EXPERIENCE_PREVIOUS_RANK_KEY, groupId);
    }
}