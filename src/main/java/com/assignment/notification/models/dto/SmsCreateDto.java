package com.assignment.notification.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor  @NoArgsConstructor
@Builder
public class SmsCreateDto {
    @NotNull(message = "phone_number is mandatory!")
    @Size(min = 10, max = 15)
    private String phoneNumber;

    @NotNull(message = "message can't be null")
    private String message;
}
