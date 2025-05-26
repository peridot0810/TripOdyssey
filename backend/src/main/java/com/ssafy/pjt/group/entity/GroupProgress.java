package com.ssafy.pjt.group.entity;

import lombok.Data;

@Data
public class GroupProgress {
	private Boolean scheduleCoordination;
	private Boolean roleAssignment;
	private Boolean tripPlanning;
	private Boolean onTrip;
	private Boolean settlement;
	private Boolean finished;
}
