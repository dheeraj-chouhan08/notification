package com.assignment.notification.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsRequestDto {
    private Integer id;

    private LocalDateTime updated_at;

    private LocalDateTime created_at;
    @NotNull(message = "message can't be null")
    private String message;

    private String status;

    @NotNull(message = "phone_number is mandatory")
    @Size(min = 9, max = 14, message = "please provide a valid phone number")
    private String phone_number;

    private String failure_code;
    private String failure_comments;


}
