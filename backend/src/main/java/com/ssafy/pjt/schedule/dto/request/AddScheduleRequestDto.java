package com.ssafy.pjt.schedule.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddScheduleRequestDto {
	private Integer contentId;
	private Integer groupId;
}
