package com.ssafy.pjt.group.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelGroup {
	private Integer groupId;
	private String name;
	private String description;
	private TravelGroupStatus status;
	private LocalDateTime createdAt;
	private LocalDate startDate;
	private LocalDate endDate;
}
