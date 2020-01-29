package com.assignment.notification.models.smsapi;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Getter @Setter
public class Destination {
    private List<String> msisdn = new ArrayList<>();
    private String correlationId;

    public Destination(String correlationId, String phone_number) {
        this.correlationId = correlationId;
        this.msisdn.add(phone_number);
    }
}
