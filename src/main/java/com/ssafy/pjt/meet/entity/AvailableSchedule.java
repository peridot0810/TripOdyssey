package com.ssafy.pjt.meet.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableSchedule {
	private Integer id;
	private @NonNull String userId;
	private @NonNull String groupId;
	private @NonNull LocalDate startDate;
	private @NonNull LocalDate endDate;

}
