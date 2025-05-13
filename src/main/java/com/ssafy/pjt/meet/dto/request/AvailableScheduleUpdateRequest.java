package com.ssafy.pjt.meet.dto.request;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AvailableScheduleUpdateRequest {
	private Integer id;
	private LocalDate startDate;
	private LocalDate endDate;
}
