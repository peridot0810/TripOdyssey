package com.ssafy.pjt.schedule.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateScheduleRequestWrapperDto {

	@Schema(description="비공식에서 공식으로 변한 일정 카드 리스트")
	private List<NewOfficialScheduleRequestDto> newOfficialSchedules;
	
	@Schema(description="순서가 변경된 일정 카드 리스트")
	private List<ModifiedOfficialScheduleRequestDto> modifiedOfficialSchedules;
	
	@Schema(description="공식에서 비공식으로 변한 일정 카드 리스트")
	private List<RemovedOfficialScheduleRequestDto> removedOfficialSchedules;
}
