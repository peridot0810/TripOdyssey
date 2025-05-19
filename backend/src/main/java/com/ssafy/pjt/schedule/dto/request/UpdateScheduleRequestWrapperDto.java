package com.ssafy.pjt.schedule.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class UpdateScheduleRequestWrapperDto {

	private List<NewOfficialScheduleRequestDto> newOfficialSchedules;
	private List<ModifiedOfficialScheduleRequestDto> modifiedOfficialSchedules;
	private List<RemovedOfficialScheduleRequestDto> removedOfficialSchedules;
}
