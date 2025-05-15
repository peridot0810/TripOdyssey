package com.ssafy.pjt.group.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.group.entity.GroupMemberInfo;
import com.ssafy.pjt.group.mapper.TravelGroupMemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultTravelGroupMemberService implements TravelGroupMemberService {
	
	private final TravelGroupMemberMapper memberMapper;

	@Override
	public CommonResponse<Void> inviteMember(Integer groupId, String userEmail) {
		memberMapper.insertUserToGroup(groupId, userEmail);
		// member not found exception 처리 필요
		return new CommonResponse<>(true, "사용자를 그룹 멤버로 초대 완료", null);
	}

	@Override
	public CommonResponse<Void> removeMember(Integer groupId, String userId) {
		memberMapper.deleteUserFromGroup(groupId, userId);
		return new CommonResponse<>(true, "그룹에서 멤버를 제거 완료", null);
	}

	@Override
	public CommonResponse<Void> assignMemberRole(Integer groupId, String userId, Integer roleId) {
		memberMapper.updateGroupUserRole(groupId, userId, roleId);
		return new CommonResponse<>(true, "그룹 멤버 역할 변경 완료", null);
	}

	@Override
	public CommonResponse<List<GroupMemberInfo>> getAllMembers(Integer groupId) {
		List<GroupMemberInfo> members = memberMapper.selectAllGroupUserInfo(groupId);
		return new CommonResponse<>(true, "그룹 멤버 리스트 조회 완료", members);
	}
}
