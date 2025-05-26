package com.ssafy.pjt.group.service;

import java.util.List;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.financial.dto.request.MemberInviteRequestDto;
import com.ssafy.pjt.group.dto.request.AssignMemberRoleRequestDto;
import com.ssafy.pjt.group.dto.request.RoleRequestDto;
import com.ssafy.pjt.group.dto.response.InvitedMemberResponseDto;
import com.ssafy.pjt.group.dto.response.RoleRequestResponseDto;
import com.ssafy.pjt.group.entity.GroupMemberInfo;

public interface TravelGroupMemberService {
	public CommonResponse<Void> insertMember(String inviterId, Integer groupId, String userId);
	public CommonResponse<Void> removeMember(String requesterId, Integer groupId, String userId);
	public CommonResponse<Void> assignMemberRole(String requesterId, Integer groupId, String userId, Integer roleId, Boolean accept);
	public CommonResponse<Void> insertMemberExpenseInfo(String inviterId, Integer groupId, String userId);
	public CommonResponse<List<GroupMemberInfo>> getAllMembers(String userId, Integer groupId);
	public CommonResponse<Void> memberInvite(Integer groupId, String userId, MemberInviteRequestDto memberInviteRequest);
	public List<InvitedMemberResponseDto> getInvitedMemberList(Integer groupId, String userId);
	public CommonResponse<Void> roleRequest(Integer groupId, String userId, RoleRequestDto roleRequest);
	public List<RoleRequestResponseDto> getRoleRequestList(Integer groupId, String userId);
	public void AssignMemberRole(Integer groupId, String masterId, AssignMemberRoleRequestDto assignMemberRoleRequest);
}
