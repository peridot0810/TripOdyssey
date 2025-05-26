package com.ssafy.pjt.group.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleAssignRequestDto {
	private String userId;
	private Integer groupId;
	private Integer roleId;
}
