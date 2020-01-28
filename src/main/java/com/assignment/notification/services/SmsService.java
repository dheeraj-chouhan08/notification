package com.assignment.notification.services;

import com.assignment.notification.dto.SmsRequestDto;
import com.assignment.notification.entities.SmsRequests;
import com.assignment.notification.models.SmsCreateDto;
import com.assignment.notification.repositories.SmsRequestsRepository;
import org.slf4j.Logger;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class SmsService {
    private static final Logger logger = LoggerFactory.getLogger(kafkaProducerService.class);
    @Autowired
    private SmsRequestsRepository smsRequestsRepository;

    public String addSmsRequest(@Valid SmsCreateDto smsCreateDto){

        SmsRequests smsRequests = new SmsRequests(smsCreateDto.getMessage(), smsCreateDto.getPhone_number());
        smsRequestsRepository.save(smsRequests);
        logger.info(String.format("#### new message request entry saved ."));
        return smsRequests.getId().toString();
        }

    public  SmsRequestDto getSmsRequest(Integer request_id){
        if(smsRequestsRepository.findById(request_id).isPresent()){

            SmsRequests smsRequests = smsRequestsRepository.findById(request_id).get();

            return new SmsRequestDto(smsRequests.getId(),  smsRequests.getUpdated_at(), smsRequests.getCreated_at(),smsRequests.getMessage(), smsRequests.getStatus(), smsRequests.getPhone_number(), smsRequests.getFailure_code(), smsRequests.getFailure_comments());
        }
        else {
            return null;
        }
    }
    public ArrayList<SmsRequestDto> getSmsRequests(ArrayList<Integer> list){
        ArrayList<SmsRequests> smsRequestsList = (ArrayList<SmsRequests>) smsRequestsRepository.findAllById(list);
        ArrayList<SmsRequestDto> resultList = new ArrayList<>();
        for(SmsRequests smsRequests: smsRequestsList){
            SmsRequestDto smsRequestDto =  new SmsRequestDto(smsRequests.getId(),  smsRequests.getUpdated_at(), smsRequests.getCreated_at(),smsRequests.getMessage(), smsRequests.getStatus(), smsRequests.getPhone_number(), smsRequests.getFailure_code(), smsRequests.getFailure_comments());
            resultList.add(smsRequestDto);
        }
        return resultList;
    }


    public void updateMessageOnSuccess(Integer id){
        Optional<SmsRequests> optional = smsRequestsRepository.findById(id);
        SmsRequests smsRequests = optional.get();
        smsRequests.setStatus("queued");
        smsRequests.setUpdated_at(LocalDateTime.now());
        smsRequestsRepository.save(smsRequests);
    }

    public void updateMessageOnFailure(Integer id, String code, String comments){
        Optional<SmsRequests> optional = smsRequestsRepository.findById(id);
        SmsRequests smsRequests = optional.get();
        smsRequests.setStatus("failed");
        smsRequests.setFailure_code(code);
        smsRequests.setFailure_comments(comments);
        smsRequests.setUpdated_at(LocalDateTime.now());
        smsRequestsRepository.save(smsRequests);

    }
}
