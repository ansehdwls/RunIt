package com.zoku.ui.model

enum class PhoneWatchConnection(val route: String) {
    EMPTY(""),
    START_ACTIVITY("/start-activity"),
    START_RUNNING("/start-running"),
    PAUSE_RUNNING("/pause-running"),
    STOP_RUNNING("/stop-running"),
    RESUME_RUNNING("/resume-running"),
    SEND_BPM("/send-bpm");

    companion object{
        fun getType(route : String) : PhoneWatchConnection? =
            PhoneWatchConnection.entries.find { it.route == route }
    }


}