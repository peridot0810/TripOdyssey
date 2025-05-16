package com.ssafy.pjt.transportation.entity;

import lombok.Data;

@Data
public class Transportation {
	private Integer typeId;
	private Integer groupId;
	private String departure;
	private String arrival;
	private String from;
	private String to;
	private String description;
}
