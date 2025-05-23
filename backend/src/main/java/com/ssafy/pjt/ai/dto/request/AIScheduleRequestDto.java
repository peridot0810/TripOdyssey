package com.ssafy.pjt.ai.dto.request;

import java.util.List;

import com.ssafy.pjt.ai.entity.SimpleSchedule;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIScheduleRequestDto {
    private String message;
    
    private List<SimpleSchedule> scheduleList;
}
