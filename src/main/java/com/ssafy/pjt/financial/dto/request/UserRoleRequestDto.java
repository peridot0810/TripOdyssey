package com.ssafy.pjt.financial.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRoleRequestDto {
	private String userId;
	private Integer groupId;
}
