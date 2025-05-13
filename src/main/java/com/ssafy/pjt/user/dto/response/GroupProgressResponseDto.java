package com.ssafy.pjt.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupProgressResponseDto {
	private Boolean scheduleCoordination;
	private Boolean roleAssignment;
	private Boolean tripPlanning;
	private Boolean onTrip;
	private Boolean settlement;
	private Boolean finished;
}
