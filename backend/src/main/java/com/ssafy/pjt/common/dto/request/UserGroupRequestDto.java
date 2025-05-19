package com.ssafy.pjt.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGroupRequestDto {
	private String userId;
	private Integer groupId;
}
