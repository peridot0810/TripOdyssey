package com.ssafy.pjt.ai.dto.response;

import java.util.List;

import com.ssafy.pjt.ai.entity.OptimizedSchedule;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AIScheduleResponseDto {
    // AI's reasoning or summary
    private String summary;
    
    // Optimized schedule items with new day/order assignments
    private List<OptimizedSchedule> optimizedSchedules;
}
