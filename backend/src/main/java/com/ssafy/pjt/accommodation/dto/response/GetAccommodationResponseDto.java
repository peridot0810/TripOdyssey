package com.ssafy.pjt.accommodation.dto.response;

import com.ssafy.pjt.schedule.dto.response.GetAttractionResponseDto;

import lombok.Data;

@Data
public class GetAccommodationResponseDto {
	private Integer accommodationId;
	private Integer groupId;
	private Integer attractionsNo;
	private String name;
	private String checkInTime;
	private String checkOutTime;
	private GetAttractionResponseDto accommodationInfo;
}
