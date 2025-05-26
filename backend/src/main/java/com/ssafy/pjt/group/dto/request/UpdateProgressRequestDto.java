package com.ssafy.pjt.group.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateProgressRequestDto {
	@Schema(accessMode=Schema.AccessMode.READ_ONLY)
	private Integer groupId;
	
	private Boolean scheduleCoordination;
	private Boolean roleAssignment;
	private Boolean tripPlanning;
	private Boolean onTrip;
	private Boolean settlement;
	private Boolean finished;
}
