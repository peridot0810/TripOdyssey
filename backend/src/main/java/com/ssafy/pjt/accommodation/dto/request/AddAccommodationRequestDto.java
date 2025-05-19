package com.ssafy.pjt.accommodation.dto.request;

import lombok.Data;

@Data
public class AddAccommodationRequestDto {
	private Integer groupId;
	
	private String name;
	private Integer locationId;
	private String checkIn;
	private String checkOut;
}
