package com.ssafy.pjt.accommodation.dto.response;

import lombok.Data;

@Data
public class AccommodationResponseDto {
	private Integer accommodationId;
	private Integer groupId;
	private String name;
	private String checkIn;
	private String checkOut;
	private String locatoinId;
}
