package com.ssafy.pjt.transportation.dto.response;

import lombok.Data;

@Data
public class TransportationResponseDto {
	private Integer transportationId;
	private Integer typeId;
	private Integer groupId;
	private String departure;
	private String arrival;
	private String from;
	private String to;
	private String description;
}
