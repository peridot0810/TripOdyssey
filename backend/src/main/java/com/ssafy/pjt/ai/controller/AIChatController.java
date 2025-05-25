package com.ssafy.pjt.ai.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.ai.dto.request.AIScheduleRequestDto;
import com.ssafy.pjt.ai.dto.response.AIScheduleResponseDto;
import com.ssafy.pjt.ai.helper.RestControllerHelper;
import com.ssafy.pjt.ai.service.AiChatService;
import com.ssafy.pjt.ai.service.ScheduleAiChatService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ai")
@Slf4j
public class AIChatController implements RestControllerHelper {
    
    private final AiChatService chatService;
    private final ScheduleAiChatService scheduleAiChatService;
    
    // Manual constructor injection with @Qualifier
    public AIChatController(@Qualifier("basicAiChatService") AiChatService chatService,
                           ScheduleAiChatService scheduleAiChatService) {
        this.chatService = chatService;
        this.scheduleAiChatService = scheduleAiChatService;
    }
    
    @PostMapping("/simple")
    ResponseEntity<?> simpleGeneration(@RequestBody Map<String, String> userInput) {
        Object result = chatService.simpleGeneration(userInput.get("message"));
        return handleSuccess(Map.of("message", result));
    }
    
    
    
    
    
    @PostMapping("/optimize-schedule")
    ResponseEntity<?> optimizeSchedule(@RequestBody AIScheduleRequestDto requestDto) {
        log.info("Received schedule optimization request with {} schedules", 
                 requestDto.getScheduleList() != null ? requestDto.getScheduleList().size() : 0);
        
        try {
            // Validate request
            if (requestDto.getScheduleList() == null || requestDto.getScheduleList().isEmpty()) {
                log.warn("Empty schedule list received");
                return ResponseEntity.badRequest().body(Map.of("error", "일정 목록이 비어있습니다"));
            }
            
            // Call AI service to optimize schedule
            AIScheduleResponseDto response = scheduleAiChatService.scheduleGeneration(requestDto);
            
            log.info("Schedule optimization completed successfully");
            return handleSuccess(response);
            
        } catch (Exception e) {
            log.error("Error during schedule optimization: ", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "일정 최적화 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
    
}