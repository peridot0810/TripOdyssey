package com.ssafy.pjt.group.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.exception.UnauthorizedRoleAccessException;
import com.ssafy.pjt.common.exception.UserNotInGroupException;
import com.ssafy.pjt.common.service.UserValidationService;
import com.ssafy.pjt.config.OpenApiConfig;
import com.ssafy.pjt.financial.dto.request.MemberInviteRequestDto;
import com.ssafy.pjt.group.dto.response.InvitedMemberResponseDto;
import com.ssafy.pjt.group.entity.GroupMemberInfo;
import com.ssafy.pjt.group.mapper.TravelGroupMemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultTravelGroupMemberService implements TravelGroupMemberService {
	
	private final TravelGroupMemberMapper memberMapper;
	private final UserValidationService userValidationService;

	@Override
	public CommonResponse<Void> inviteMember(Integer groupId, String userEmail) {
		memberMapper.insertUserToGroup(groupId, userEmail);
		// member not found exception 처리 필요
		return new CommonResponse<>(true, "사용자를 그룹 멤버로 초대 완료", null);
	}

	@Override
	public CommonResponse<Void> removeMember(String requesterId, Integer groupId, String userId) {
		
		// 유저 확인
		if(!userValidationService.isUserInGroup(requesterId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		if(!userValidationService.isUserRoleValid(requesterId, groupId, MemberRole.MASTER.getId())) {
			throw new UnauthorizedRoleAccessException("멤버 강퇴는 방장만 요청 가능합니다.");
		}
		
		// 비즈니스 로직 
		memberMapper.deleteUserFromGroup(groupId, userId);
		return new CommonResponse<>(true, "그룹에서 멤버를 제거 완료", null);
	}

	@Override
	public CommonResponse<Void> assignMemberRole(String requesterId, Integer groupId, String userId, Integer roleId) {
		
		// 유저 확인
		if(!userValidationService.isUserInGroup(requesterId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		if(!userValidationService.isUserRoleValid(requesterId, groupId, MemberRole.MASTER.getId())) {
			throw new UnauthorizedRoleAccessException("멤버 역할 임명은 방장만 요청 가능합니다.");
		}
		
		// 비즈니스 로직
		memberMapper.updateGroupUserRole(groupId, userId, roleId);
		return new CommonResponse<>(true, "그룹 멤버 역할 변경 완료", null);
	}

	@Override
	public CommonResponse<List<GroupMemberInfo>> getAllMembers(Integer groupId) {
		List<GroupMemberInfo> members = memberMapper.selectAllGroupUserInfo(groupId);
		return new CommonResponse<>(true, "그룹 멤버 리스트 조회 완료", members);
	}
	
	@Override
	public CommonResponse<Void> memberInvite(Integer groupId, String userId,
			MemberInviteRequestDto memberInviteRequest) {
		
		// 유저 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		if(userValidationService.isUserRoleValid(userId, groupId, MemberRole.NORMAL.getId())) {
			throw new UnauthorizedRoleAccessException("일반 그룹원은 다른 그룹원을 초대할 수 없습니다.");
		}
		
		
		// 비즈니스 로직
		memberInviteRequest.setGroupId(groupId);
		memberInviteRequest.setSenderId(userId);
		memberMapper.memberInvite(memberInviteRequest);
		return null;
	}
	
	@Override
	public List<InvitedMemberResponseDto> getInvitedMemberList(Integer groupId, String userId) {
		
		// 유저 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		if(userValidationService.isUserRoleValid(userId, groupId, MemberRole.NORMAL.getId())) {
			throw new UnauthorizedRoleAccessException("일반 그룹원은 초대 목록을 조회할 수 없습니다.");
		}
		
		// 비즈니스 로직
		return memberMapper.getInvitedMemberList(groupId);
	}
}
