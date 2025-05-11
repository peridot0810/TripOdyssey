package com.ssafy.pjt.user.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponseDto {
	private String email;
	private String gender;
	private Integer age;
	
	// exp
	private Integer leaderExp;
	private Integer scheduleExp;
	private Integer financeExp;
	private Integer logisticsExp;
	private Integer memberExp;
}
