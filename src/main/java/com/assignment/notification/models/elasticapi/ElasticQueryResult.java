package com.assignment.notification.models.elasticapi;

import com.assignment.notification.dto.SmsRequestDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class ElasticQueryResult {
    private Map<Integer, SmsRequestDto> resultMap = new HashMap<>();
    private String scrollId;
    public ElasticQueryResult(ArrayList<SmsRequestDto> resultList,  String scrollId){

        Integer count = 1;
        for(SmsRequestDto smsRequestDto : resultList){
            this.resultMap.put(count, smsRequestDto);
            count++;
        }

        this.scrollId = scrollId;
    }


}
