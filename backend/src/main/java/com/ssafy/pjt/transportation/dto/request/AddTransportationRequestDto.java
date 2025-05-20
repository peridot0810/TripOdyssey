package com.ssafy.pjt.transportation.dto.request;

import lombok.Data;

@Data
public class AddTransportationRequestDto {
	private Integer typeId;
	private Integer groupId;
	private String departure;
	private String arrival;
	private String from;
	private String to;
	private String description;
	private Integer transportationId;
}
