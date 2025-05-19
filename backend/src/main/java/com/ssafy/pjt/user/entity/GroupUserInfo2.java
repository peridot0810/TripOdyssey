package com.ssafy.pjt.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserInfo2 {
	private String userId;
	private Integer groupId;
	private Integer roleId;
}
