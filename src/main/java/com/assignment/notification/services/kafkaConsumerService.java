package com.assignment.notification.services;

import com.assignment.notification.dto.SmsRequestDto;
import com.assignment.notification.models.smsapi.RootSms;
import com.assignment.notification.models.thirdparty.response.FailedResponse;
import com.assignment.notification.models.thirdparty.response.Response;
import com.assignment.notification.models.thirdparty.response.SuccessfulResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class kafkaConsumerService {

    private final Logger logger = LoggerFactory.getLogger(kafkaConsumerService.class);



    @Autowired
    SmsService smsService;

    @Autowired
    ElasticsearchService elasticsearchService;


    private SmsRequestDto smsRequestDto;

    Jedis jedis = new Jedis();

    @KafkaListener(topics = "notification.send_sms", groupId = "notification")

    public void consume(String request_id) throws IOException {

        logger.info(String.format("########### -> Consumed message -> %s", request_id));

        smsRequestDto  = smsService.getSmsRequest(Integer.parseInt(request_id));

        String phone_number = smsRequestDto.getPhone_number();

        // ***** if phone_number is blacklisted nothing to do *******
        if(jedis.sismember("blacklist", phone_number)){
            logger.info(String.format("####### -> Sms can't be sent since its blacklisted number !!!"));

        }

        else {

            logger.info(String.format("####### -> Sms is ready to sent ..."));

            final String uriAddress = "https://api.imiconnect.in/resources/v1/messaging ";

            RootSms rootSms = new RootSms(smsRequestDto.getMessage(), smsRequestDto.getPhone_number(), request_id);

            ObjectMapper mapper = new ObjectMapper();// pretty print
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootSms);
            logger.info(json);
            logger.info("");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type" , "application/json");
            httpHeaders.set("key", "7b73f76d-369e-11ea-6ty7-9e4e-025282c394f232");

            HttpEntity<RootSms> request = new HttpEntity<>(rootSms, httpHeaders);

            RestTemplate restTemplate = new RestTemplate();


            logger.info(String.format("#### message request successfully indexed !!"));

            try {
                logger.info(" ******* trying to get successful response from third api...");

                ResponseEntity<SuccessfulResponse> response = restTemplate.postForEntity(uriAddress, request, SuccessfulResponse.class);

                logger.info(response.getBody().toString());

                ArrayList<Response> responseList = (ArrayList<Response>) response.getBody().getResponse();
                for (Response response1 : responseList) {

                    elasticsearchService.indexSmsElastic(smsRequestDto);   // index msg details only if sent.

                    smsService.updateMessageOnSuccess(smsRequestDto.getId());      //updating status field of message_request in DB
                }
            }
            catch (RestClientException ex) {

                logger.info(" ********  trying to get a failed response from third api...");

                ResponseEntity<FailedResponse> failedResponse = restTemplate.postForEntity(uriAddress,request,FailedResponse.class);

                logger.info(failedResponse.getBody().toString());

                String failure_code, failure_comments;
                failure_code = failedResponse.getBody().getResponse().getCode();
                failure_comments = failedResponse.getBody().getResponse().getDescription();
                smsService.updateMessageOnFailure(smsRequestDto.getId(), failure_code, failure_comments);

            }




        }
    }
}
