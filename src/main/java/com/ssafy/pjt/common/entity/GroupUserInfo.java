package com.ssafy.pjt.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserInfo {
	private String userId;
	private Integer groupId;
	private Integer roleId;
}
