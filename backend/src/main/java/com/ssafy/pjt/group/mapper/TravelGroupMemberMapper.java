package com.ssafy.pjt.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.pjt.financial.dto.request.MemberInviteRequestDto;
import com.ssafy.pjt.group.dto.request.HandleRoleRequestDto;
import com.ssafy.pjt.group.dto.request.RoleRequestDto;
import com.ssafy.pjt.group.dto.response.InvitedMemberResponseDto;
import com.ssafy.pjt.group.dto.response.RoleRequestResponseDto;
import com.ssafy.pjt.group.entity.GroupMemberInfo;

@Mapper
public interface TravelGroupMemberMapper {
	
	public void insertUserToGroup(
			@Param("groupId") Integer groupId, 
			@Param("userId") String userId);
	
	public void insertMemberExpenseInfo(
			@Param("groupId") Integer groupId, 
			@Param("userId") String userId);
	
	public void deleteUserFromGroup(
			@Param("groupId") Integer groupId, 
			@Param("userId") String userId);
	
	public void addGroupUserRole(
			@Param("groupId") Integer groupId, 
			@Param("userId") String userId,
			@Param("roleId") Integer roleId);
	
	public List<GroupMemberInfo> selectAllGroupUserInfo(Integer groupId);
	
	// 멤버 초대 
	public void memberInvite(MemberInviteRequestDto memberInviteRequest);
	public List<InvitedMemberResponseDto> getInvitedMemberList(Integer groupId);
	
	// 역할 신청
	public void roleRequest(RoleRequestDto roleRequest);
	public List<RoleRequestResponseDto> getRoleRequestList(Integer groupId);
	public Integer handleRoleRequest(HandleRoleRequestDto handleRoleRequest);
}
