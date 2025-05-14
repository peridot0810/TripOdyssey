package com.ssafy.pjt.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;

@Mapper
public interface CommonMapper {
	Integer getUserRoleInGroup(UserGroupRequestDto userRoleInGroupRequest);
	Boolean checkUserInGroup(UserGroupRequestDto checkUserInGroupRequest);
}
