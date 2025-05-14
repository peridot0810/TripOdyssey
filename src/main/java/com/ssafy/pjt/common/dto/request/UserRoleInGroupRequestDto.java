package com.ssafy.pjt.common.dto.request;

import lombok.Data;

@Data
public class UserRoleInGroupRequestDto {
	private String userId;
	private Integer groupId;
}
