package com.assignment.notification.models.smsapi;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Getter @Setter
public class Destination {
    private List<String> msisdn = new ArrayList<>();
    private String corelationid;

    public Destination(String corelationid, String phone_number) {
        this.corelationid = corelationid;
        this.msisdn.add(phone_number);
    }

    public List<String> getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(List<String> msisdn) {
        this.msisdn = msisdn;
    }

    public String getCorelationid() {
        return corelationid;
    }

    public void setCorelationid(String corelationid) {
        this.corelationid = corelationid;
    }
}
