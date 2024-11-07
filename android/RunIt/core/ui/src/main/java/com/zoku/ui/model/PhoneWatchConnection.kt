package com.zoku.ui.model

enum class PhoneWatchConnection(val route: String) {
    START_ACTIVITY("/start-activity"),
    START_RUNNING("/start-running"),
    PAUSE_RUNNING("/pause-running"),
    STOP_RUNNING("/stop-running"),
    RESUME_RUNNING("/resume-running"),
}