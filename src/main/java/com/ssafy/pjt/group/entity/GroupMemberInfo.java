package com.ssafy.pjt.group.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberInfo {
	private String userId;
	private String nickName;
	private String roleName;
}
