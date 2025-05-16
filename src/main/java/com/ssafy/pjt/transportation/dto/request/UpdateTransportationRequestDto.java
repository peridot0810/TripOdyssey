package com.ssafy.pjt.transportation.dto.request;

import lombok.Data;

@Data
public class UpdateTransportationRequestDto {
	private Integer transportationId;
	private Integer typeId;
	private String departure;
	private String arrival;
	private String from;
	private String to;
	private String description;
}
