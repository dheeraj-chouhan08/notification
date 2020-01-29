package com.assignment.notification.models.thirdparty.response;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder

public class Response {
    private String code;

    private String description;

    private String transid;

    private String correlationid;


}
