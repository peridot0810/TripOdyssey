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
import com.ssafy.pjt.group.dto.request.AssignMemberRoleRequestDto;
import com.ssafy.pjt.group.dto.request.HandleRoleRequestDto;
import com.ssafy.pjt.group.dto.request.RoleAssignRequestDto;
import com.ssafy.pjt.group.dto.request.RoleRemovalRequestDto;
import com.ssafy.pjt.group.dto.request.RoleRequestDto;
import com.ssafy.pjt.group.dto.response.InvitedMemberResponseDto;
import com.ssafy.pjt.group.dto.response.RoleRequestResponseDto;
import com.ssafy.pjt.group.entity.GroupMemberInfo;
import com.ssafy.pjt.group.mapper.TravelGroupMemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultTravelGroupMemberService implements TravelGroupMemberService {
	
	private final TravelGroupMemberMapper memberMapper;
	private final UserValidationService userValidationService;
 
	@Override
	public CommonResponse<Void> insertMember(String inviterId, Integer groupId, String userId) {
		
		log.debug("inserMember : inviterId {}, groupId {} ", inviterId, groupId);
		
		// 유저 확인
		if(!userValidationService.isUserInGroup(inviterId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		if(userValidationService.isUserRoleValid(inviterId, groupId, MemberRole.NORMAL.getId())) {
			throw new UnauthorizedRoleAccessException("일반 그룹원은 다른 그룹원을 추가할 수 없습니다.");
		}
		
		memberMapper.insertUserToGroup(groupId, userId);
		// member not found exception 처리 필요
		return new CommonResponse<>(true, "사용자를 그룹 멤버로 추가 완료", null);
	}
	
	@Override
	public CommonResponse<Void> insertMemberExpenseInfo(String inviterId, Integer groupId, String userId) {
		// 유저 확인
		if(!userValidationService.isUserInGroup(inviterId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		if(userValidationService.isUserRoleValid(inviterId, groupId, MemberRole.NORMAL.getId())) {
			throw new UnauthorizedRoleAccessException("일반 그룹원은 다른 그룹원을 추가할 수 없습니다.");
		}
		
		memberMapper.insertMemberExpenseInfo(groupId, userId);
		return null;
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
	public CommonResponse<Void> assignMemberRole(String requesterId, Integer groupId, String userId, Integer roleId, Boolean accept) {
		
		// 유저 확인
		if(!userValidationService.isUserInGroup(requesterId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		if(!userValidationService.isUserRoleValid(requesterId, groupId, MemberRole.MASTER.getId())) {
			throw new UnauthorizedRoleAccessException("멤버 역할 임명은 방장만 요청 가능합니다.");
		}
		
		// 비즈니스 로직
		// group_role_request 테이블 데이터 수정
		Integer changedRowCnt = memberMapper.handleRoleRequest(HandleRoleRequestDto.builder()
				.userId(userId)
				.groupId(groupId)
				.accept(accept)
				.roleId(roleId)
				.build());
		
		if(changedRowCnt==0) {
			return new CommonResponse<>(true, "해당 역할 신청이 존재하지 않습니다.", null);
		}
		
		// group_user_info에 데이터 추가 
		if(accept) {
			memberMapper.addGroupUserRole(groupId, userId, roleId);
			return new CommonResponse<>(true, "그룹 멤버 역할 추가 완료", null);
		}else {
			return new CommonResponse<>(true, "그룹 멤버 역할 신청을 거절했습니다.", null);
		}
	}

	@Override
	public CommonResponse<List<GroupMemberInfo>> getAllMembers(String userId, Integer groupId) {
		// 유저 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		
		// 비즈니스 로직
		List<GroupMemberInfo> members = memberMapper.selectAllGroupUserInfo(groupId);
		
		for(GroupMemberInfo member :members) {
			member.setRoles(userValidationService.getUserRoles(member.getUserId(), groupId));
		}
		
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
	
	@Override
	public CommonResponse<Void> roleRequest(Integer groupId, String userId, RoleRequestDto roleRequest) {
		// 유저 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		if(userValidationService.isUserRoleValid(userId, groupId, roleRequest.getRoleId())) {
			throw new UnauthorizedRoleAccessException("해당 역할은 이미 임명된 상태입니다.");
		}
		
		roleRequest.setGroupId(groupId);
		roleRequest.setUserId(userId);
		memberMapper.roleRequest(roleRequest);
		return null;
	}
	
	@Override
	public List<RoleRequestResponseDto> getRoleRequestList(Integer groupId, String userId) {
		// 유저 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.MASTER.getId())) {
			throw new UnauthorizedRoleAccessException("역할 신청 목록은 방장만 조회 가능합니다.");
		}
		return memberMapper.getRoleRequestList(groupId);
	}
	
	@Override
	public void AssignMemberRole(Integer groupId, String masterId, AssignMemberRoleRequestDto assignMemberRoleRequest) {
		
		// 유저 확인
		if(!userValidationService.isUserInGroup(masterId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		if(!userValidationService.isUserRoleValid(masterId, groupId, MemberRole.MASTER.getId())) {
			throw new UnauthorizedRoleAccessException("역할 임명은 방장만 할 수 있습니다.");
		}
		
		//비즈니스 로직
		// 역할 임명
		RoleAssignRequestDto roleAssignment = RoleAssignRequestDto.builder()
					.userId(assignMemberRoleRequest.getUserId())
					.groupId(groupId)
					.build();
		
		for(Integer assignRole : assignMemberRoleRequest.getAssignRoleIdList()) {
			roleAssignment.setRoleId(assignRole);
			memberMapper.roleAssignment(roleAssignment);
		}
		
		// 역할 박탈
		RoleRemovalRequestDto roleRemoval = RoleRemovalRequestDto.builder()
				.userId(assignMemberRoleRequest.getUserId())
				.groupId(groupId)
				.build();
		for(Integer removalRole : assignMemberRoleRequest.getRemovalRoleIdList()) {
			roleRemoval.setRoleId(removalRole);
			memberMapper.roleRemoval(roleRemoval);
		}
		
		
	}
}
