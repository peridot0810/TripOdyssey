package com.ssafy.pjt.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.common.entity.GroupUserInfo;

@Mapper
public interface CommonMapper {
//	Integer GetUserRoleInGroup(UserGroupRequestDto userRoleInGroupRequest);
	
	List<Integer> getUserRoleInGroup(UserGroupRequestDto userRoleInGroupRequest);
	
	Boolean checkUserInGroup(UserGroupRequestDto checkUserInGroupRequest);
	
	Boolean checkUserRoleAndGroup(GroupUserInfo groupUserInfo);
}
