package com.ssafy.pjt.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.common.dto.request.UserRoleInGroupRequestDto;

@Mapper
public interface CommonMapper {
	Integer getUserRoleInGroup(UserRoleInGroupRequestDto userRoleInGroupRequest);
}
