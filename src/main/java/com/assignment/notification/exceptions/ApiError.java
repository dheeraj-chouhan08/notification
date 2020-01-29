package com.assignment.notification.exceptions;

import lombok.*;


@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class ApiError {
    private Error error;

}
