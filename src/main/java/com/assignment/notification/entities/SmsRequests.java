package com.assignment.notification.entities;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sms_requests")
public class SmsRequests {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;


    @NotNull(message = "message can't be empty !")
    private String message;

    private String status;

    @NotNull(message = "phone_number is mandatory")
    @Size(min = 9, max = 13, message = "please provide a valid phone number")
    private String phone_number;

    private String failure_code;
    private String failure_comments;



    public SmsRequests(String message, String phone_number) {
        this.message = message;
        this.phone_number = phone_number;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
        this.failure_code = "";
        this.failure_comments = "";
        this.status = "created";
    }


}
