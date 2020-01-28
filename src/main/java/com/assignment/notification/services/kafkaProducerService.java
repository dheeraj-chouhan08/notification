package com.assignment.notification.services;

import org.slf4j.Logger;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class kafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(kafkaProducerService.class);
    private static final String TOPIC = "notification.send_sms";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String request_id){
        logger.info(String.format("#### -> Producing message -> %s", request_id));
        this.kafkaTemplate.send(TOPIC, request_id);
    }
}
