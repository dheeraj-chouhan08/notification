package com.assignment.notification.models.elasticapi;

import com.assignment.notification.models.dto.SmsRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ElasticQueryResult {
    private ArrayList<SmsRequestDto> resultList = new ArrayList<>();

    private String scrollId;

}
