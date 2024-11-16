package com.ssafy.runit.domain.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ssafy.runit.domain.fcm.dto.SendFcmMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class FcmServiceImpl implements FcmService {
    @Override
    public void sendNotification(SendFcmMessage message) {
        try {
            Notification notification = Notification.builder().setTitle(message.getTitle()).setBody(message.getBody()).setImage(message.getImage()).build();
            Map<String, String> dataPayload = new HashMap<>();
            dataPayload.put("title", message.getTitle());
            dataPayload.put("body", message.getBody());
            dataPayload.put("link", message.getLink());
            Message sendMessage = Message.builder().setToken(message.getToken()).putAllData(dataPayload).setNotification(notification).build();
            FirebaseMessaging.getInstance().send(sendMessage);
        } catch (FirebaseMessagingException e){
            log.warn("Fcm Token Error : {}!",message.getToken());
        }
        catch (Exception e) {
            log.warn("알 수 없는 내부 오류입니다.");
        }
    }
}
