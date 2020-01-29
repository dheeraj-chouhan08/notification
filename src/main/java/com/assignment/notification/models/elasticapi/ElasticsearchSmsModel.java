package com.assignment.notification.models.elasticapi;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ElasticsearchSmsModel {
    private String phoneNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String message;
}
