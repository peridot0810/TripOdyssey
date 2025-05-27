package com.ssafy.pjt.group.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserExperienceRequestDto {
	private String userId;
	
	private Integer leaderExp;
	private Integer scheduleExp;
	private Integer financeExp;
	private Integer logisticsExp;
	private Integer memberExp;
}
