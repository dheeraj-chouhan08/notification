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
    private String phone_number;


    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private String message;
}
