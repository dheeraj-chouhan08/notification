package com.assignment.notification.models.smsapi;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter

public class RootSms {

    private String deliverychannel;

    private Channels channels;

    private List<Destination>destination = new ArrayList<>();



    public RootSms(String text, String phone_number, String corelationid) {
        this.deliverychannel = "sms";
        this.channels = new Channels(text);
        Destination destination = new Destination(corelationid, phone_number);
        this.destination.add(destination);
    }

}
