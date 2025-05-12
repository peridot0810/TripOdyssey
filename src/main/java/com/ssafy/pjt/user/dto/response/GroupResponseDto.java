package com.ssafy.pjt.user.dto.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GroupResponseDto {
	private String groupId;
	private String name;
	private String status;
	private Integer memberCount;
	private String myRole;
	private String startDate;
	private String endDate;
	private GroupProgressResponseDto progress;
}