package com.assignment.notification.models.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SmsRequestDto {
    private Integer id;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
    @NotNull(message = "message can't be null")
    private String message;

    private String status;

    @NotNull(message = "phone_number is mandatory")
    @Size(min = 10, max = 15, message = "please provide a valid phone number")
    private String phoneNumber;

    private String failureCode;
    private String failureComments;


}
