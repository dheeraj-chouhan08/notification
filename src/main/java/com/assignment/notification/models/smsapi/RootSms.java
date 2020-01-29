package com.assignment.notification.models.smsapi;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RootSms {

    private String deliverychannel;

    private Channels channels;

    private List<Destination>destination = new ArrayList<>();

    public RootSms(String text, String phoneNumber, String correlationId) {
        this.deliverychannel = "sms";
        this.channels = new Channels(text);
        Destination destination = new Destination(correlationId, phoneNumber);
        this.destination.add(destination);
    }


}
