package com.assignment.notification.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class RecordNotFoundException extends Exception{
    private String message;
    private  Integer statusCode;


}
