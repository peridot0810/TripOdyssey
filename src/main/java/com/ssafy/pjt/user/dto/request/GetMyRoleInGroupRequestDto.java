package com.ssafy.pjt.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetMyRoleInGroupRequestDto {
	private String userId;
	private String groupId;
}
