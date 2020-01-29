package com.assignment.notification.services;

import com.assignment.notification.models.dto.SmsRequestDto;
import com.assignment.notification.models.smsapi.RootSms;

import com.assignment.notification.models.thirdparty.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.SQLException;

import static com.assignment.notification.constants.NotificationConstants.RedisConstants.BLACKLISTED_KEY;

@Service
@Slf4j
public class kafkaConsumerService {

    @Autowired
    SmsService smsService;

    @Autowired
    ElasticsearchService elasticsearchService;

    @Autowired
    TPAService tpaService;

    @Autowired
     Jedis jedis;


    @KafkaListener(topics = "notification.send_sms", groupId = "notification")
    public void consume(String requestId) throws IOException, SQLException {

        log.info(String.format("########### -> Consumed message -> %s", requestId));
        SmsRequestDto smsRequestDto = smsService.getSmsRequest(Integer.parseInt(requestId));
        Boolean isBlacklisted = jedis.sismember(BLACKLISTED_KEY, smsRequestDto.getPhoneNumber());
        if (!isBlacklisted) {
            RootSms rootSms   = new RootSms(smsRequestDto.getMessage(), smsRequestDto.getPhoneNumber(), requestId);
            Response response =  tpaService.sendMessage(rootSms);
            if (response.getCode().equals("1001")) {

                elasticsearchService.indexSmsElastic(smsRequestDto);       // index message details
                smsService.updateMessageOnSuccess(smsRequestDto.getId());  // updating status field of messageRequest in DB

            } else {
                smsService.updateMessageOnFailure(smsRequestDto.getId(), response.getCode(), response.getDescription()); //updating status and failure details in DB
            }
        } else {
            smsService.updateMessageOnBlacklisted(smsRequestDto.getId());
        }
    }
}
