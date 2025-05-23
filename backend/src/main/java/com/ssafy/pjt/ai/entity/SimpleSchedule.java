package com.ssafy.pjt.ai.entity;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleSchedule {
	
    private Long contentId;
    private Long attractionsNo;
    
    private String name;
    private String description;
    private String contentTypeName;
    
    private Double latitude;
    private Double longitude;
    private String addr1;
    
    private Integer day;
    private Integer order;
    
    private String overview;
}
