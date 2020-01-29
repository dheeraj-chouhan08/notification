package com.assignment.notification.services;

import com.assignment.notification.models.dto.MessageSentResponseDto;
import com.assignment.notification.models.dto.SmsCreateDto;
import com.assignment.notification.models.dto.SmsRequestDto;
import com.assignment.notification.models.entities.SmsRequests;
import com.assignment.notification.repositories.SmsRequestsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


@Slf4j
@Service
public class SmsService {

    @Autowired
    private SmsRequestsRepository smsRequestsRepository;

    public MessageSentResponseDto addSmsRequest(SmsCreateDto smsCreateDto) {

        SmsRequests smsRequest = SmsRequests.builder()
                .message(smsCreateDto.getMessage())
                .phoneNumber(smsCreateDto.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        smsRequestsRepository.save(smsRequest);
        log.debug(String.format("#### new messageRequest entry saved ."));
        MessageSentResponseDto messageSentResponseDto = new MessageSentResponseDto().builder()
                .requestId(smsRequest.getId().toString())
                .comments("Successfully Sent")
                .build();
        return messageSentResponseDto;
    }

    public SmsRequestDto getSmsRequest(Integer requestId) throws SQLException {

        try {
            SmsRequests smsRequests = smsRequestsRepository.findById(requestId).get();
            return SmsRequestDto.builder()
                    .id(smsRequests.getId())
                    .message(smsRequests.getMessage())
                    .createdAt(smsRequests.getCreatedAt())
                    .updatedAt(smsRequests.getUpdatedAt())
                    .failureCode(smsRequests.getFailureCode())
                    .failureComments(smsRequests.getFailureComments())
                    .phoneNumber(smsRequests.getPhoneNumber())
                    .status(smsRequests.getStatus())
                    .build();
        } catch (Exception ex) {
            throw new SQLException();
        }
    }

    public ArrayList<SmsRequestDto> getSmsRequests(ArrayList<Integer> list) {

        ArrayList<SmsRequests> smsRequestsList = (ArrayList<SmsRequests>) smsRequestsRepository.findAllById(list);
        ArrayList<SmsRequestDto> resultList = new ArrayList<>();
        for (SmsRequests smsRequests : smsRequestsList) {

            SmsRequestDto smsRequestDto = SmsRequestDto.builder()
                    .message(smsRequests.getMessage())
                    .failureCode(smsRequests.getFailureCode())
                    .createdAt(smsRequests.getCreatedAt())
                    .updatedAt(smsRequests.getUpdatedAt())
                    .status(smsRequests.getStatus())
                    .failureComments(smsRequests.getFailureComments())
                    .id(smsRequests.getId())
                    .phoneNumber(smsRequests.getPhoneNumber())
                    .build();
            resultList.add(smsRequestDto);
        }
        return resultList;
    }


    public void updateMessageOnSuccess(Integer id) {
        Optional<SmsRequests> optional = smsRequestsRepository.findById(id);
        SmsRequests smsRequests = optional.get();
        smsRequests.setStatus("queued");
        smsRequests.setUpdatedAt(LocalDateTime.now());
        smsRequestsRepository.save(smsRequests);
    }

    public void updateMessageOnFailure(Integer id, String code, String comments) {
        SmsRequests smsRequests = smsRequestsRepository.findById(id).get();
        smsRequests.setStatus("failed");
        smsRequests.setFailureCode(code);
        smsRequests.setFailureComments(comments);
        smsRequests.setUpdatedAt(LocalDateTime.now());
        smsRequestsRepository.save(smsRequests);
    }

    public void updateMessageOnBlacklisted(Integer id){
        SmsRequests smsRequests = smsRequestsRepository.findById(id).get();
        smsRequests.setStatus("failed");
        smsRequests.setUpdatedAt(LocalDateTime.now());
        smsRequests.setFailureComments("PhoneNumber is blacklisted");
        smsRequestsRepository.save(smsRequests);
    }
}
