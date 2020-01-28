package com.assignment.notification.exception.handler;

import com.assignment.notification.exceptions.PhoneNumberNotFoundException;
import com.assignment.notification.exceptions.RecordNotFoundException;
import com.assignment.notification.exceptions.ApiError;
import com.assignment.notification.exceptions.Error;
import org.slf4j.Logger;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;


@ControllerAdvice
public class GlobalExceptionHandler  {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "request_id not found")
    @ExceptionHandler(SQLException.class)
    public  void handleSQLException( ){
        logger.error("---------------**** SQLException handler executed ****-----------");
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "phone_number not found")
    @ExceptionHandler(PhoneNumberNotFoundException.class)
    public void handlePhoneNumberNotFoundException(){
        logger.error("---------------**** PhoneNumberNotFoundException handler executed ****-----------");
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "records not found")
    @ExceptionHandler(RecordNotFoundException.class)
    public void handleRecordNotFoundException(){
        logger.error("---------------**** RecordNotFoundException handler executed ****-----------");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        String parameter = ex.getParameter().getParameterName();
        logger.info("---------------**** MethodArgumentNotValidException handler executed ****-----------");

        ApiError apiError = new ApiError();
        Error error = new Error();

        if(parameter.contentEquals("smsCreateDto")){
            error.setMessage("phone_number is mandatory");
            error.setCode("Invalid_Request");
            apiError.setError(error);
            return buildResponseEntity(apiError);
        }
        error.setMessage("arguments not valid");
        error.setCode("Invalid_Request");
        return buildResponseEntity(apiError);
    }


    private ResponseEntity buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<ApiError>(apiError,HttpStatus.NOT_FOUND);
    }

}
