package com.ssafy.pjt.meet.dto.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailableSchedulePostResponse {
	private Integer id;
	private LocalDate startDate;
	private LocalDate endDate;
}
