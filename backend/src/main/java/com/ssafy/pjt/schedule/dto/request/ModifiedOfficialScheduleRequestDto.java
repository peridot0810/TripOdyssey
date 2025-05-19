package com.ssafy.pjt.schedule.dto.request;

import lombok.Data;

@Data
public class ModifiedOfficialScheduleRequestDto {
	private Integer order;
	private Integer day;
	private Integer contentId;
}
