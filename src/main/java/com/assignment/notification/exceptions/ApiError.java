package com.assignment.notification.exceptions;

import lombok.*;


@Data @AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private Error error;

}
