package com.ssafy.pjt.user.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GroupResponseDto {
	private String groupId;
	private String name;
	private String status;
	private Integer memberCount;
	private List<String> myRole;
	private LocalDate startDate;
	private LocalDate endDate;
	private GroupProgressResponseDto progress;
	private String imageUrl;
}