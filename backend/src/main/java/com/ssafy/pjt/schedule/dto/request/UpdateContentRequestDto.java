package com.ssafy.pjt.schedule.dto.request;

import lombok.Data;

@Data
public class UpdateContentRequestDto {
	private Integer contentId;	
	private String name;
	private String description;
	private Integer attractionNo;
}
