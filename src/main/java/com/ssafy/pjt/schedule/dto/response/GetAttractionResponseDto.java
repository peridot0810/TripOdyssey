package com.ssafy.pjt.schedule.dto.response;

import lombok.Data;

@Data
public class GetAttractionResponseDto {
	private Integer no; 
	private String title; 
	private String contentType; 
	private String firstImage1; 
	private String firstImage2; 
	private Double latitude; 
	private Double longitude; 
	private String tel; 
	private String addr1; 
	private String addr2; 
	private String homepage; 
	private String overview;
}
