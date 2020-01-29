package com.assignment.notification.controller;

import com.assignment.notification.exceptions.RecordNotFoundException;
import com.assignment.notification.models.elasticapi.ElasticQueryResult;
import com.assignment.notification.models.elasticapi.ElasticSmsInterval;
import com.assignment.notification.services.ElasticsearchService;
import com.assignment.notification.services.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@Slf4j
@RequestMapping("/v1")
public class ElasticsearchController {

    @Autowired
    ElasticsearchService elasticsearchServices;

    @Autowired
    SmsService smsService;

    /* *************** for searching all messages sent to  a phone_number containing given text ************ */
    @RequestMapping(path = "get/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ElasticQueryResult> getAllMessage(@RequestParam(value = "text") String text,
                                                            @RequestParam(value = "cursor", required = false) String scrollId,
                                                            @RequestParam(value = "limit", required = false) String limit) throws IOException, RecordNotFoundException {

        try {

            log.debug(String.format("trying......"));
            ElasticQueryResult elasticQueryResult = elasticsearchServices.getMessages(text, scrollId, limit);
            return new ResponseEntity<ElasticQueryResult>(elasticQueryResult, HttpStatus.OK);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    /* *************** for searching all messages sent to phone_number within given datetime range ************ */
    @RequestMapping(path = "get/messages/interval", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ElasticQueryResult> getMessagesInterval(@RequestBody ElasticSmsInterval elasticSmsInterval) throws IOException, RecordNotFoundException {
        try {
            ElasticQueryResult elasticQueryResult = elasticsearchServices.getMessagesInterval(elasticSmsInterval);
            return new ResponseEntity<ElasticQueryResult>(elasticQueryResult, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
