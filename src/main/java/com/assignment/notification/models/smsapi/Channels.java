package com.assignment.notification.models.smsapi;

import lombok.*;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class Channels {
    private Sms sms;
    public Channels(String text){
        this.sms = new Sms(text);
    }

}



