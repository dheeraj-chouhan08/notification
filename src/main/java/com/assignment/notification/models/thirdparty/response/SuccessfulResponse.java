package com.assignment.notification.models.thirdparty.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class SuccessfulResponse {
    private List<Response> response;


}

