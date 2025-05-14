package com.ssafy.pjt.group.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ssafy.pjt.group.dto.response.TravelGroupPostResponse;
import com.ssafy.pjt.group.entity.RoleLimits;
import com.ssafy.pjt.group.entity.TravelGroupStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelGroupUpdateRequest {
	private String name;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;
	private RoleLimits roleLimits;
}
