package com.assignment.notification.models.elasticapi;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
public class ElasticSmsInterval {
    private String phone_number;
    private LocalDateTime created_from;
    private LocalDateTime created_to;
    private String scrollId;
    private Integer limit;


}
