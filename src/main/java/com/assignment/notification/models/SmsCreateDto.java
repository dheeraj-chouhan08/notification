package com.assignment.notification.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data @AllArgsConstructor
public class SmsCreateDto {
    @NotNull(message = "phone_number is mandatory!")
    @Size(min = 10, max = 13)
    private String phone_number;

    @NotNull(message = "message can't be null")
    private String message;
}
