package com.ssafy.pjt.group.service;

import java.util.List;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.financial.dto.request.MemberInviteRequestDto;
import com.ssafy.pjt.group.dto.response.InvitedMemberResponseDto;
import com.ssafy.pjt.group.entity.GroupMemberInfo;

public interface TravelGroupMemberService {
	public CommonResponse<Void> inviteMember(Integer groupId, String userId);
	public CommonResponse<Void> removeMember(Integer groupId, String userId);
	public CommonResponse<Void> assignMemberRole(Integer groupId, String userId, Integer roleId);
	public CommonResponse<List<GroupMemberInfo>> getAllMembers(Integer groupId);
	public CommonResponse<Void> memberInvite(Integer groupId, String userId, MemberInviteRequestDto memberInviteRequest);
	public List<InvitedMemberResponseDto> getInvitedMemberList(Integer groupId, String userId);
}
