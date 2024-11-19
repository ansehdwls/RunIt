package com.ssafy.runit.domain.fcm.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendFcmMessage {
    private String link;
    private String title;
    private String body;
    private String image;
    private String token;
}
