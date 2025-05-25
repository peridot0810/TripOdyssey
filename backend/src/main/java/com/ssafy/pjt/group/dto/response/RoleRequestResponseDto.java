package com.ssafy.pjt.group.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleRequestResponseDto {
	private String userId;
	private Integer groupId;
	private Integer roleId;
	private String requestedAt;
	private String status;
}
