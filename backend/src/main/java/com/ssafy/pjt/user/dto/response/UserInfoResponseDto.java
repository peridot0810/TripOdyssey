package com.ssafy.pjt.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
	private String email;
	private String gender;
	private Integer age;
	private String userId;
	private String nickname;
	private String profileImage;
	
	// exp
	private Integer leaderExp;
	private Integer scheduleExp;
	private Integer financeExp;
	private Integer logisticsExp;
	private Integer memberExp;
}
