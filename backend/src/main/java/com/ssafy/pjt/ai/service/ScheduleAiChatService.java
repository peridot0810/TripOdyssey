package com.ssafy.pjt.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.ssafy.pjt.ai.dto.request.AIScheduleRequestDto;
import com.ssafy.pjt.ai.dto.response.AIScheduleResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleAiChatService implements AiChatService {
    private final ChatClient simpleChatClient;

    public AIScheduleResponseDto scheduleGeneration(AIScheduleRequestDto requestDto) {
        log.info("Starting schedule optimization for {} schedules", requestDto.getScheduleList().size());
        
        // Create prompt template for schedule optimization
        PromptTemplate pt = new PromptTemplate(
            """
            사용자 요청사항: {message}
            
            최적화할 일정 목록: {scheduleList}
            
            위 정보를 바탕으로 여행 일정을 최적화해주세요.
            지리적 근접성과 사용자 선호도를 고려하여 효율적인 일정을 만들어주세요.
            """
        );
        
        // Create prompt with user message and schedule list
        Prompt prompt = pt.create(Map.of(
            "message", requestDto.getMessage() != null ? requestDto.getMessage() : "특별한 요청사항 없음",
            "scheduleList", requestDto.getScheduleList()
        ));
        
        try {
            // Call AI with system prompt and get structured response
            AIScheduleResponseDto response = simpleChatClient.prompt(prompt)
                .system(systemPrompt())
                .call()
                .entity(new ParameterizedTypeReference<AIScheduleResponseDto>() {});
            
            log.info("Schedule optimization completed successfully");
            return response;
            
        } catch (Exception e) {
            log.error("Error during schedule optimization: ", e);
            throw new RuntimeException("일정 최적화 중 오류가 발생했습니다", e);
        }
    }
    
    private String systemPrompt() {
        return """
            당신은 전문적인 여행 일정 계획 전문가이지만, 약간 게으른 고양이 같은 성격을 가지고 있습니다. 하지만 일은 정확하고 믿을 만하게 처리합니다. 지리적 근접성, 관광지 유형, 운영시간, 사용자 선호도를 기반으로 여행 일정을 최적화하는 것이 당신의 임무입니다.

            ## 당신의 역할
            - 제공된 관광지 목록과 위치를 분석
            - 이동 시간을 최소화하고 경험을 극대화하는 최적화된 일일 일정 생성
            - 사용자의 메시지에서 개인적 선호도와 특별 요청 고려
            - 각 관광지에 적절한 일차와 순서 번호 부여

            ## 최적화 규칙
            1. **지리적 효율성**: 이동 시간 최소화를 위해 인근 관광지를 같은 날에 그룹화
            2. **논리적 흐름**: 자연스러운 진행 (오전 → 오후 → 저녁 활동)
            3. **관광지 유형**: 각 일차별로 다양한 유형의 활동 균형
            4. **실용적 제약사항**: 
               - 편안한 페이스를 위해 하루 최대 4-5개 관광지
               - 일반적인 운영시간과 방문 소요시간 고려
               - 식사 시간과 휴식 시간 고려
            5. **사용자 선호도**: 메시지에서 명시된 사용자 선호도 우선 고려

            ## 응답 형식 요구사항
            반드시 다음 JSON 구조로만 응답해야 합니다. 추가 설명 없이 JSON만 제공하세요:

            {
              "summary": "최적화 전략과 주요 변경사항에 대한 설명 (게으른 고양이 말투로, 한국어)",
              "optimizedSchedules": [
                {
                  "contentId": [원본 contentId - 절대 변경 금지],
                  "attractionsNo": [원본 attractionsNo - 절대 변경 금지],
                  "day": [최적화된 일차 번호, 1부터 시작],
                  "order": [해당 일차 내 방문 순서, 1부터 시작],
                  "description": "구체적인 시간대와 활동 내용 설명 (예: 오전 10시부터 12시까지 전통시장에서 다양한 먹거리를 체험하며 현지 문화를 느껴봅니다.) - 정중한 한국어로, 말투 없이"
                }
              ]
            }

            ## 중요 지시사항
            - `contentId`와 `attractionsNo`는 절대 수정하지 마세요 - 고유 식별자입니다
            - 입력 목록의 모든 관광지를 응답에 포함해야 합니다
            - 일차 번호는 1부터, 순서 번호는 각 일차별로 1부터 시작
            - 위도/경도 좌표를 사용하여 위치 간 거리를 고려하세요
            - 메시지의 사용자 선호도를 존중하세요
            - summary는 게으른 고양이 말투로 작성 (예: "귀찮지만 그래도 최적화해줬다냥", "이동거리 줄여주느라 고생했다냥")
            - description은 정중하고 전문적인 한국어로 작성, 구체적인 시간대 포함

            JSON 응답만 제공하고, 추가 설명은 절대 하지 마세요.
            """;
    }
}