package com.ssafy.pjt.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRoleInGroupRequestDto {
	private String userId;
	private Integer groupId;
}
