package com.assignment.notification.services;

import com.assignment.notification.dto.SmsRequestDto;
import com.assignment.notification.models.elasticapi.ElasticQueryResult;
import com.assignment.notification.models.elasticapi.ElasticSmsInterval;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class ElasticsearchServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);

    @Autowired
    RestHighLevelClient client;

    @Autowired
    SmsService smsService;

    SmsRequestDto smsRequestDto;

    public void indexSmsElastic(SmsRequestDto smsRequestDto) throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", smsRequestDto.getId());
        jsonMap.put("created_at", smsRequestDto.getCreated_at());
        jsonMap.put("updated_at", smsRequestDto.getUpdated_at());
        jsonMap.put("message", smsRequestDto.getMessage());
        jsonMap.put("phone_number", smsRequestDto.getPhone_number());

        logger.info(jsonMap.toString());

        IndexRequest indexRequest = new IndexRequest("smsrequestelastic", "smsrequestdata")
                .source(jsonMap);

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        logger.info(indexResponse.toString());
    }


    public ElasticQueryResult getMessages(String text, String scrollId, String limit) throws IOException {

        logger.info(String.format("getMessages Executing... "));
        SearchHits hits;
        if (scrollId == null) {

            MatchQueryBuilder queryBuilder1 = new MatchQueryBuilder("message", text);
          //  MatchAllQueryBuilder queryBuilder1 = new MatchAllQueryBuilder();
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


            sourceBuilder.query(queryBuilder1);

            if (limit != null)
                sourceBuilder.size(Integer.parseInt(limit));

            SearchRequest searchRequest = new SearchRequest("smsrequestelastic");
            searchRequest.source(sourceBuilder);

            searchRequest.scroll(TimeValue.timeValueMinutes(2));
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            logger.info(searchResponse.toString());
            scrollId = searchResponse.getScrollId();
            hits = searchResponse.getHits();

        }
        else {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueSeconds(50));
            SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchScrollResponse.getScrollId();
            hits = searchScrollResponse.getHits();
        }
        TotalHits totalHits = hits.getTotalHits();
        long numHits = totalHits.value;
        logger.info(String.valueOf(numHits));

        logger.info(String.format("#################"));

        SearchHit[] searchHits = hits.getHits();

        ArrayList<Integer> idList = new ArrayList<>();

         for(SearchHit hit : searchHits)  {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Integer id = (Integer) sourceAsMap.get("id");
            logger.info("id is " + id);
            idList.add(id);

        }
        logger.info(String.valueOf(idList.size()));

         ArrayList<SmsRequestDto> resultList;
        resultList = smsService.getSmsRequests(idList);

        for (SmsRequestDto smsRequestDto : resultList) {
            logger.info(String.valueOf(smsRequestDto));
        }

        ElasticQueryResult elasticQueryResult = new ElasticQueryResult(resultList, scrollId);
        logger.info(String.format(elasticQueryResult.toString()));
        return elasticQueryResult;
    }


    public ElasticQueryResult getMessagesInterval(ElasticSmsInterval elasticSmsInterval) throws IOException {
        logger.info("getMessagesInterval executing...");

        SearchHits hits;
        String scrollId;
        if (elasticSmsInterval.getScrollId() == "") {
            logger.info(String.format("if part executing....."));
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("phone_number", elasticSmsInterval.getPhone_number());

            RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("created_at").gte(elasticSmsInterval.getCreated_from()).lte(elasticSmsInterval.getCreated_to());

            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                    .must(matchQueryBuilder)
                    .must(rangeQueryBuilder);


            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(boolQueryBuilder);
            if(elasticSmsInterval.getLimit() != null){
                sourceBuilder.size(elasticSmsInterval.getLimit());
            }

            SearchRequest searchRequest = new SearchRequest("smsrequestelastic");
            searchRequest.source(sourceBuilder);

            searchRequest.scroll(TimeValue.timeValueMinutes(100));
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            hits = searchResponse.getHits();

        } else {
            logger.info(String.format("else part executing....."));
            SearchScrollRequest scrollRequest = new SearchScrollRequest(elasticSmsInterval.getScrollId());
            scrollRequest.scroll(TimeValue.timeValueSeconds(100));
            SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchScrollResponse.getScrollId();
            hits = searchScrollResponse.getHits();
        }
        TotalHits totalHits = hits.getTotalHits();
        long numHits = totalHits.value;

        logger.info(String.format("#################"));

        SearchHit[] searchHits = hits.getHits();

        ArrayList<Integer> idList = new ArrayList<>();

        for (SearchHit hit : searchHits) {

            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            Integer id = (Integer) sourceAsMap.get("id");
            idList.add(id);

        }
        logger.info(String.format(String.valueOf(idList.size())));
        ArrayList<SmsRequestDto> resultList;
        resultList = smsService.getSmsRequests(idList);
        logger.info(String.format(String.valueOf(resultList.size())));
        for (SmsRequestDto smsRequestDto : resultList) {
            logger.info(String.valueOf(smsRequestDto));
        }
        ElasticQueryResult elasticQueryResult = new ElasticQueryResult(resultList, scrollId);
        logger.info(String.format(elasticQueryResult.toString()));
        return elasticQueryResult;
    }
}
