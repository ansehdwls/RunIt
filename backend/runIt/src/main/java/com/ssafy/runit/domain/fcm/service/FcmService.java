package com.ssafy.runit.domain.fcm.service;

import com.ssafy.runit.domain.fcm.dto.SendFcmMessage;

public interface FcmService {

    void sendNotification(SendFcmMessage message);

}
