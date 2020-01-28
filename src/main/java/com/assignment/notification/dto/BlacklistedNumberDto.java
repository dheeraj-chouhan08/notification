package com.assignment.notification.dto;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistedNumberDto {
    private Set<String> phone_numbers;
    
}
