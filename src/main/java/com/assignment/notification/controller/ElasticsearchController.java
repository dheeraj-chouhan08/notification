package com.assignment.notification.controller;

import com.assignment.notification.exceptions.RecordNotFoundException;
import com.assignment.notification.models.elasticapi.ElasticQueryResult;
import com.assignment.notification.models.elasticapi.ElasticSmsInterval;
import com.assignment.notification.services.ElasticsearchService;
import com.assignment.notification.services.SmsService;
import org.slf4j.Logger;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/v1")
public class ElasticsearchController {

    private  static  final Logger logger = LoggerFactory.getLogger(ElasticsearchController.class);

    @Autowired
    ElasticsearchService elasticsearchServices;

    @Autowired
    SmsService smsService;

    /* *************** for searching all messages sent to  a phone_number containing given text ************ */
    @RequestMapping(path = "get/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ElasticQueryResult> getAllMessage(@RequestParam(value = "text") String text,
                                                            @RequestParam (value = "_paging_token", required = false) String scrollId,
                                                            @RequestParam(value = "limit", required = false) String limit)  throws IOException, RecordNotFoundException {

        try {

            logger.info(String.format("trying......"));
            ElasticQueryResult elasticQueryResult = elasticsearchServices.getMessages(text, scrollId, limit);
            return new ResponseEntity<ElasticQueryResult>(elasticQueryResult, HttpStatus.OK);

        } catch (Exception ex) {
            throw new RecordNotFoundException("no record", 404);
        }

    }

    /* *************** for searching all messages sent to phone_number within given datetime range ************ */
    @RequestMapping(path = "get/messages/interval", method = RequestMethod.GET, produces =  MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ElasticQueryResult> getMessagesInterval(@RequestBody ElasticSmsInterval elasticSmsInterval) throws IOException, RecordNotFoundException {
        try {
            logger.info("trying....");
            ElasticQueryResult elasticQueryResult = elasticsearchServices.getMessagesInterval(elasticSmsInterval);
            return new ResponseEntity<ElasticQueryResult>(elasticQueryResult, HttpStatus.OK);
        }
        catch (Exception ex){
            throw new RecordNotFoundException("no record", 404);
        }
    }

}
