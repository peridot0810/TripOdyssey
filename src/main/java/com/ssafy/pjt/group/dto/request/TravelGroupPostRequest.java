package com.ssafy.pjt.group.dto.request;

import java.time.LocalDate;

import com.ssafy.pjt.group.entity.RoleLimits;
import com.ssafy.pjt.group.entity.TravelGroupStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelGroupPostRequest {
	private String name;
	private String description;
	private TravelGroupStatus status;
	private LocalDate startDate;
	private LocalDate endDate;
	private RoleLimits roleLimits;
}
