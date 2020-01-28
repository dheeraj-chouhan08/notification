package com.assignment.notification.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class RecordNotFoundException extends Exception{
    private String message;
    private  Integer statusCode;


}
