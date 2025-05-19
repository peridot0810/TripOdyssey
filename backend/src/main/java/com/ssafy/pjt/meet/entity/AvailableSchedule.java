package com.ssafy.pjt.meet.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class AvailableSchedule {
	private Integer id;

	//@NonNull
	private String userId;

	//@NonNull
	private String groupId;

	//@NonNull
	private LocalDate startDate;

	//@NonNull
	private LocalDate endDate;
}

