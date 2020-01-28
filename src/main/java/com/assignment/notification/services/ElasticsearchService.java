package com.assignment.notification.services;

import com.assignment.notification.dto.SmsRequestDto;
import com.assignment.notification.exceptions.RecordNotFoundException;
import com.assignment.notification.models.elasticapi.ElasticQueryResult;
import com.assignment.notification.models.elasticapi.ElasticSmsInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticsearchService {

    private  static final Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);

    @Autowired
    ElasticsearchServiceImpl elasticsearchService;

    public void indexSmsElastic(SmsRequestDto smsRequestDto) throws IOException {
        elasticsearchService.indexSmsElastic(smsRequestDto);
    }

    public ElasticQueryResult getMessages(String text, String scrollId, String limit) throws IOException, RecordNotFoundException {
        logger.info(String.format("in ElasticsearchService"));
        try {
            ElasticQueryResult elasticQueryResult = elasticsearchService.getMessages(text, scrollId, limit);
            logger.info(elasticQueryResult.toString());
            return elasticQueryResult;
        }
        catch (Exception ex){
            throw new RecordNotFoundException("no record", 404);
        }
    }

    public ElasticQueryResult getMessagesInterval(ElasticSmsInterval elasticSmsInterval) throws IOException, RecordNotFoundException {
        logger.info(String.format("in ElasticService"));
        try {
            ElasticQueryResult elasticQueryResult = elasticsearchService.getMessagesInterval(elasticSmsInterval);
            logger.info(elasticQueryResult.toString());
            return elasticQueryResult;
        }
        catch (Exception ex){
            throw new RecordNotFoundException("no record", 404);
        }
    }
}
