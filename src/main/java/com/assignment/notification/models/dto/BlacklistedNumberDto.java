package com.assignment.notification.models.dto;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlacklistedNumberDto {
    private Set<String> phoneNumbers;
    
}
