package com.assignment.notification.services;

import com.assignment.notification.models.dto.SmsRequestDto;
import com.assignment.notification.models.elasticapi.ElasticQueryResult;
import com.assignment.notification.models.elasticapi.ElasticSmsInterval;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.assignment.notification.constants.NotificationConstants.ElasticConstants.INDEX_NAME_SMS_ELASTIC;
import static com.assignment.notification.constants.NotificationConstants.ElasticConstants.TYPE_NAME_SMS_ELASTIC;

@Service
@Slf4j
public class ElasticsearchService {

    @Autowired
    RestHighLevelClient client;

    @Autowired
    SmsService smsService;

    public void indexSmsElastic(SmsRequestDto smsRequestDto) throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", smsRequestDto.getId());
        jsonMap.put("created_at", smsRequestDto.getCreatedAt());
        jsonMap.put("updated_at", smsRequestDto.getUpdatedAt());
        jsonMap.put("message", smsRequestDto.getMessage());
        jsonMap.put("phone_number", smsRequestDto.getPhoneNumber());

        log.debug(jsonMap.toString());
        IndexRequest indexRequest = new IndexRequest(INDEX_NAME_SMS_ELASTIC, TYPE_NAME_SMS_ELASTIC)
                .source(jsonMap);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        log.debug(indexResponse.toString());
    }


    public ElasticQueryResult getMessages(String text, String scrollId, String limit) throws IOException {

        log.debug(String.format("getMessages Executing... "));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(new MatchQueryBuilder("message", text));
        if (limit != null) {
            searchSourceBuilder.size(Integer.parseInt(limit));
        } else {
            searchSourceBuilder.size(5);
        }
        return searchHelper(searchSourceBuilder, scrollId);
    }

    public ElasticQueryResult getMessagesInterval(ElasticSmsInterval elasticSmsInterval) throws IOException {

        log.debug("getMessagesInterval executing...");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(new BoolQueryBuilder()
                        .must(new MatchQueryBuilder("phone_number", elasticSmsInterval.getPhoneNumber()))
                        .must(new RangeQueryBuilder("created_at")
                                .gte(elasticSmsInterval.getCreatedFrom())
                                .lte(elasticSmsInterval.getCreatedTo())));

        if (elasticSmsInterval.getLimit() != null) {
            searchSourceBuilder.size(elasticSmsInterval.getLimit());
        } else {
            searchSourceBuilder.size(5);
        }
        return searchHelper(searchSourceBuilder, elasticSmsInterval.getScrollId());
    }

    public ElasticQueryResult searchHelper(SearchSourceBuilder searchSourceBuilder, String scrollId) throws IOException {

        SearchHits hits;
        if (scrollId == null || scrollId == "") {
            SearchRequest searchRequest = new SearchRequest(INDEX_NAME_SMS_ELASTIC);
            searchRequest.source(searchSourceBuilder);
            searchRequest.scroll(TimeValue.timeValueMinutes(2));
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            hits = searchResponse.getHits();

        } else {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueSeconds(50));
            SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchScrollResponse.getScrollId();
            hits = searchScrollResponse.getHits();
        }
        TotalHits totalHits = hits.getTotalHits();
        long numHits = totalHits.value;
        log.debug(String.valueOf(numHits));
        log.debug(String.format("#################"));

        SearchHit[] searchHits = hits.getHits();
        ArrayList<Integer> idList = new ArrayList<>();

        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            idList.add((Integer) sourceAsMap.get("id"));
        }

        ArrayList<SmsRequestDto> resultList;
        resultList = smsService.getSmsRequests(idList);
        ElasticQueryResult elasticQueryResult = new ElasticQueryResult().builder()
                .resultList(resultList)
                .scrollId(scrollId)
                .build();

        return elasticQueryResult;
    }
}
