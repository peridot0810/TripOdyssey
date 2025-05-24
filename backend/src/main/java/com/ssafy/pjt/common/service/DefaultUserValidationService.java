package com.ssafy.pjt.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.common.entity.GroupUserInfo;
import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.mapper.CommonMapper;
import com.ssafy.pjt.user.entity.GroupUserInfo2;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultUserValidationService implements UserValidationService {
	private final CommonMapper commonMapper;
	
	private final Integer ROLE_COUNT = 5;	// 현재 역할 개수 5개
	
	public boolean isUserRoleValid(String userId, Integer groupId, Integer roleId) {
		GroupUserInfo groupUserInfo = GroupUserInfo.builder()
				.userId(userId)
				.groupId(groupId)
				.roleId(roleId)
				.build();
		
		return commonMapper.checkUserRoleAndGroup(groupUserInfo);
	}

	@Override
	public boolean isUserInGroup(String userId, Integer groupId) {
		return commonMapper.checkUserInGroup(UserGroupRequestDto.builder()
				.userId(userId)
				.groupId(groupId)
				.build());
	};
	
	@Override
	public Boolean[] getUserRoles(String userId, Integer groupId) {
		List<Integer> roleList = commonMapper.getUserRoleInGroup(UserGroupRequestDto.builder()
				.userId(userId)
				.groupId(groupId)
				.build());
		
		Boolean[] retList = new Boolean[ROLE_COUNT];
		for(Integer roleId : roleList) {
			retList[roleId - 1] = true;
		}
		return retList;
	}
}
