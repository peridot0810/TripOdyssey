package com.ssafy.pjt.accommodation.dto.request;

import lombok.Data;

@Data
public class UpdateAccommodationDto {
	private Integer accommodationId;
	
	private String name; 
	private String checkIn;
	private String checkOut;
	private Integer locationId;
	
}
