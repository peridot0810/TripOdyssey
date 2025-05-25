package com.ssafy.pjt.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import com.ssafy.pjt.ai.dto.response.AiTransportationResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransportationAiChatService implements AiChatService{
	private final ChatClient simpleChatClient;
	
	
	public AiTransportationResponseDto transportationGeneration(String userInput, MimeType mime, Resource resource) {
        log.info("Starting transportation generation");
        
        
        try {
            // Call AI with system prompt and get structured response
            AiTransportationResponseDto response = simpleChatClient.prompt()
                .system(systemPrompt())
                .user(spec -> spec.media(mime, resource).text(userInput))
                .call()
                .entity(new ParameterizedTypeReference<AiTransportationResponseDto>() {});
            
            log.info("Transportation Genertation completed successfully");
            return response;
            
        } catch (Exception e) {
            log.error("Error during transportation generation: ", e);
            throw new RuntimeException("교통편 생성 중 오류가 발생했습니다", e);
        }
    }
	
	
	private String systemPrompt() {
        return """
            당신은 사용자가 업로드한 교통편 예약 사진을 분석하여 교통편 정보를 추출하는 AI입니다.

			당신의 목표는 이미지를 기반으로 아래의 JSON 형식으로 정보를 반환하는 것입니다.
			
			반드시 다음 형식의 JSON으로만 응답해야 합니다:
			
			{
			  "typeId": 정수 (1~9 중 하나),
			  "departure": "출발 시각, 형식: YYYY-MM-DD HH:MM:SS",
			  "arrival": "도착 시각, 형식: YYYY-MM-DD HH:MM:SS",
			  "from": "출발 지역 (예: 서울역, 인천국제공항)",
			  "to": "도착 지역 (예: 부산역, 김포공항)",
			  "description": "간단한 설명 (예: 대한항공 예약 정보입니다)"
			}
			
			### typeId 값 정의:
			1: Airplane  
			2: Train  
			3: Bus  
			4: Car  
			5: Taxi  
			6: Subway  
			7: Ferry  
			8: Bicycle  
			9: Walking
			
			반드시 각 항목을 빠짐없이 포함하며, 해당 정보가 이미지에 명확히 표시되지 않은 경우에는 빈 문자열("")을 넣으세요.
			
			예시 응답:
			{
			  "typeId": 1,
			  "departure": "2025-06-03 08:15:00",
			  "arrival": "2025-06-03 10:30:00",
			  "from": "인천국제공항",
			  "to": "제주국제공항",
			  "description": "진에어 항공권 예약 정보입니다"
			}
			
			당신은 절대로 설명이나 부가 텍스트 없이 위 JSON만 반환해야 합니다.
            """;
    }
}
