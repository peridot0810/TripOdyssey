package com.ssafy.pjt.ai.entity;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptimizedSchedule {
    private Long contentId;
    private Long attractionsNo;
    
    // AI-assigned schedule info
    private Integer day;
    private Integer order;
    
    private String description;
}
