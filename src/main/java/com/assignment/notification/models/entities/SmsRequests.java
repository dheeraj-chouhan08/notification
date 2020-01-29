package com.assignment.notification.models.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "sms_requests")
public class SmsRequests {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @NotNull(message = "message can't be empty !")
    private String message;

    @Builder.Default
    private String status = "created";

    @NotNull(message = "phone_number is mandatory")
    @Size(min = 10, max = 15, message = "please provide a valid phone number")
    private String phoneNumber;

    private String failureCode;
    private String failureComments;

}
