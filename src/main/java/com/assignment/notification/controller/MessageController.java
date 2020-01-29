package com.assignment.notification.controller;

import com.assignment.notification.models.dto.*;

import com.assignment.notification.services.SmsService;
import com.assignment.notification.services.kafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@RequestMapping("/v1")
@Slf4j
public class MessageController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private kafkaProducerService kafkaProducerService;

    /* *************** for sending given message to given phoneNumber ************ */

    @RequestMapping(path = "/sms/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageSentResponseDto> createSms(@Valid @RequestBody SmsCreateDto smsCreateDto) {

        try {
            MessageSentResponseDto messageSentResponseDto = smsService.addSmsRequest(smsCreateDto);
            this.kafkaProducerService.sendMessage(messageSentResponseDto.getRequestId());
            return new ResponseEntity<MessageSentResponseDto>(messageSentResponseDto, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Internal Server Error Occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* *************** to get smsRequest with respect to particular id ************ */

    @RequestMapping(path = "/get/sms/{requestId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SmsRequestDto> getSmsDetails(@PathVariable(value = "requestId") Integer requestId) throws SQLException {
        try {
            SmsRequestDto smsRequestDto = smsService.getSmsRequest(requestId);
            return new ResponseEntity<SmsRequestDto>(smsRequestDto, HttpStatus.OK);
        } catch (Exception ex) {

            log.error("Internal Server Error Occurred");
            throw new SQLException();
        }
    }

}