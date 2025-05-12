package com.ssafy.pjt.meet.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DateRange {
	private LocalDate start;
	private LocalDate end;
}
