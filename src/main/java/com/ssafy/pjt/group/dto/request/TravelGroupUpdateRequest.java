package com.ssafy.pjt.group.dto.request;

import java.time.LocalDate;
import com.ssafy.pjt.group.entity.RoleLimits;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelGroupUpdateRequest {
	private Integer groupId;
	private String name;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;
	private RoleLimits roleLimits;
}
