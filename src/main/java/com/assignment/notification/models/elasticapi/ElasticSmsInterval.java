package com.assignment.notification.models.elasticapi;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
public class ElasticSmsInterval {
    private String phoneNumber;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private String scrollId;
    @Builder.Default
    private Integer limit = 5;


}
