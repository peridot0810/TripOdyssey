package com.ssafy.pjt.ai.dto.response;

import lombok.Data;

@Data
public class AiTransportationResponseDto {
		  
	private Integer typeId;
	private String departure;
	private String arrival;
	private String from;
	private String to;
	private String description;
	
}
