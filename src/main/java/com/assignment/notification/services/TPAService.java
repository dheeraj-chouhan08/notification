package com.assignment.notification.services;

import com.assignment.notification.models.smsapi.RootSms;
import com.assignment.notification.models.thirdparty.response.FailedResponse;
import com.assignment.notification.models.thirdparty.response.Response;
import com.assignment.notification.models.thirdparty.response.SuccessfulResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class TPAService {

    public Response sendMessage(RootSms rootSms) throws JsonProcessingException {

        log.debug(String.format("####### -> Sms is ready to sent ..."));
        final String uriAddress = "https://api.imiconnect.in/resources/v1/messaging ";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("key", "");
        HttpEntity<RootSms> request = new HttpEntity<>(rootSms, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();

        try {
            log.debug(" ******* trying to get successful response from third api...");
            ResponseEntity<SuccessfulResponse> response = restTemplate.postForEntity(uriAddress, request, SuccessfulResponse.class);
            log.debug(response.toString());
            return response.getBody().getResponse().get(0);

        } catch (RestClientException ex) {

            log.debug(" ********  trying to get a failed response from third api...");
            ResponseEntity<FailedResponse> failedResponse = restTemplate.postForEntity(uriAddress, request, FailedResponse.class);
            log.debug(failedResponse.getBody().toString());
            return failedResponse.getBody().getResponse();

        }


    }
}
