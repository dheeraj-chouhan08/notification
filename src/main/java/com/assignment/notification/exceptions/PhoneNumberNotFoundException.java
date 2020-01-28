package com.assignment.notification.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberNotFoundException extends Exception{
    private String message;
    private Integer statusCode;

}
