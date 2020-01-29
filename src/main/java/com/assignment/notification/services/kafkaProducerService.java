package com.assignment.notification.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class kafkaProducerService {
    private static final String TOPIC = "notification.send_sms";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String request_id){
        log.info(String.format("#### -> Producing message -> %s", request_id));
        this.kafkaTemplate.send(TOPIC, request_id);
    }
}
