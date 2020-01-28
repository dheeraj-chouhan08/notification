package com.assignment.notification.controller;

import com.assignment.notification.dto.SmsRequestDto;
import com.assignment.notification.models.MessageSentResponse;
import com.assignment.notification.models.SmsCreateDto;
import com.assignment.notification.services.SmsService;
import com.assignment.notification.services.kafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
@RequestMapping("/v1")
public class MessageController {

    private  static  final Logger logger =  LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private SmsService smsService;

    @Autowired
    private  kafkaProducerService kafkaProducerService;


    /* *************** for sending given message to given phone_number ************ */

    @RequestMapping(path = "/sms/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, MessageSentResponse> > createSms(@Valid @RequestBody SmsCreateDto smsCreateDto)  {

            String request_id = smsService.addSmsRequest(smsCreateDto);
            this.kafkaProducerService.sendMessage(request_id);



            MessageSentResponse data = new MessageSentResponse(request_id);
            HashMap<String, MessageSentResponse> map = new HashMap<>();
            map.put("data",data);
            return new ResponseEntity<HashMap<String, MessageSentResponse>>(map,HttpStatus.OK);

    }

    /* *************** to get sms_request with respect to particular id ************ */

    @RequestMapping(path = "/get/sms", method =  RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus( HttpStatus.OK)
    public ResponseEntity<SmsRequestDto> getSmsDetails( @RequestParam(value = "request_id") Integer request_id) throws  SQLException {

        SmsRequestDto smsRequestDto = smsService.getSmsRequest(request_id);
        if (smsRequestDto == null) {
            throw new SQLException();
        }
        return new ResponseEntity<SmsRequestDto>(smsRequestDto, HttpStatus.OK);
    }






}