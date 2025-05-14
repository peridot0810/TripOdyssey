package com.ssafy.pjt.group.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ssafy.pjt.group.entity.RoleLimits;
import com.ssafy.pjt.group.entity.TravelGroupStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelGroupPostResponse {
	private Integer groupId;
	private String name;
	private String description;
	private TravelGroupStatus status;
	private LocalDateTime createdAt;
	private LocalDate startDate;
	private LocalDate endDate;
	private RoleLimits roleLimits;
}
