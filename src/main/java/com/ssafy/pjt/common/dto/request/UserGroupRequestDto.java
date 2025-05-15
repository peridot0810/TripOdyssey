package com.ssafy.pjt.common.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserGroupRequestDto {
	private String userId;
	private Integer groupId;
}
