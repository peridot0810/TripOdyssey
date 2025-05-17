package com.ssafy.pjt.schedule.dto.response;

import lombok.Data;

@Data
public class GetScheduleResponseDto {
	private Integer contentId;
	 private String name;
	 private Integer order;
	 private Integer day;
	 private String description;
	 private Boolean isOfficial;
	 
	 private Integer attractionsNo;
	 private GetAttractionResponseDto attractionInfo;
}
