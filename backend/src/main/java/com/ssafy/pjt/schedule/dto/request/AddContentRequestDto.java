package com.ssafy.pjt.schedule.dto.request;

import lombok.Data;

@Data
public class AddContentRequestDto {
	private Integer contentId;	
	private String name;
	private String description;
	private Integer attractionNo;
}
