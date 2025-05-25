package com.ssafy.pjt.ai.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import com.ssafy.pjt.financial.dto.response.ExpenseInfoResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseTrackerAiChatService implements AiChatService{
	private final ChatClient simpleChatClient;
	
	
	public String generateExpenseSummary(String userInput, List<ExpenseInfoResponseDto> expenseInfoList) {
		log.info("Starting expense generation");
        
        // Create prompt template for expense generation
        PromptTemplate pt = new PromptTemplate(
            """
            사용자 요청사항: {message}
            
            최적화할 일정 목록: {expenseInfoList}
            
            위 정보를 바탕으로 지출 내역을 분석/정리 해주세요.
            """
        );
        
        // Create prompt with user message and expense info list
        Prompt prompt = pt.create(Map.of(
            "message", userInput != null ? userInput : "특별한 요청사항 없음",
            "expenseInfoList", expenseInfoList
        ));
        
        try {
            // Call AI with system prompt
            String response = simpleChatClient.prompt(prompt)
                .system(systemPrompt())
                .call()
                .content();
            
            log.info("Schedule optimization completed successfully");
            return response;
            
        } catch (Exception e) {
            log.error("Error during schedule optimization: ", e);
            throw new RuntimeException("일정 최적화 중 오류가 발생했습니다", e);
        }
	}
	
	
	private String systemPrompt() {
        return """
            당신은 여행 경비를 요약하고 분석하는 똑똑한 비서입니다.

			사용자로부터 다음과 같은 형식의 지출 데이터 배열을 전달받게 됩니다:
			
			[
			  {
			    "datetime": "yyyy-MM-dd HH:mm:ss", // 지출 시각
			    "description": "string", // 지출 상세 설명
			    "amount": number, // 지출 금액 (원화)
			    "categoryName": "string", // 지출 카테고리 (예: Food, Leisure, Transportation 등)
			    "contentName": "string", // 활동 이름
			  },
			  ...
			]
			
			당신의 임무는 이 데이터를 사용자가 한눈에 보기 쉽게 분석·요약하는 것입니다.
			
			아래와 같은 항목을 포함하여 정리해주세요:
			
			1. 전체 지출 합계
			2. 카테고리별 지출 합계 및 비율
			3. 가장 많이 지출한 활동 (contentName 기준)
			4. 날짜별 총 지출 요약
			5. 사용자가 알면 좋을 분석 포인트가 있다면 추가 설명
			
			결과는 **표 형식**과 함께 **자연어 요약 문단**으로도 함께 제공해주세요.
			형식은 Markdown으로 작성해주세요. 
			
			중복된 항목은 없으며, 시간 순서대로 정렬되어 있을 수도, 아닐 수도 있습니다.
            """;
    }
}
