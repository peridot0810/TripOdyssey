package com.ssafy.pjt.accommodation.entity;

import lombok.Data;

@Data
public class AccommodationDto {
	private Integer id;
	private Integer groupId;
	private String name;
	private String checkIn;
	private String checkOut;
	private Integer locationId;
}
