package com.ssafy.pjt.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.user.dto.request.EditPasswordRequestDto;
import com.ssafy.pjt.user.dto.request.EditUserInfoRequestDto;
import com.ssafy.pjt.user.dto.request.GetMyRoleInGroupRequestDto;
import com.ssafy.pjt.user.dto.request.HandleInvitationRequestDto;
import com.ssafy.pjt.user.dto.response.GroupProgressResponseDto;
import com.ssafy.pjt.user.dto.response.InvitationResponseDto;
import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;
import com.ssafy.pjt.user.entity.Group;
import com.ssafy.pjt.user.entity.User;
import com.ssafy.pjt.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

	private final UserMapper userMapper;
	
	@Override
	public Optional<User> findByEmail(String email) {
		return userMapper.findByEmail(email);
	}
	
	@Override
	public Optional<User> findById(String userId) {
		return userMapper.findById(userId);
	}
	
	@Override
	public UserInfoResponseDto getUserInfo(String userId) {
		return userMapper.getUserInfo(userId);
	}
	
	@Override
	public int insertUser(User user) {
		return userMapper.insertUser(user);
	}
	
	@Override
	public int insertUserExp(String userId) {
		return userMapper.insertUserExp(userId);
	}
	
	@Override
	public int deleteUser(String userId) {
		return userMapper.deleteUser(userId);
	}
	
	@Override
	public int editPassword(EditPasswordRequestDto editPasswordRequest) {
		return userMapper.editPassword(editPasswordRequest);
	}
	
	@Override
	public int editUserInfo(EditUserInfoRequestDto editUserInfoRequest) {
		return userMapper.editUserInfo(editUserInfoRequest);
	}
	
	@Override
	public int activateUser(String userId) {
		return userMapper.activateUser(userId);
	}
	
	@Override
	public int deactivateUSer(String userId) {
		return userMapper.deactivateUser(userId);
	}
	
	@Override
	public boolean checkEmailExists(String email) {
		return userMapper.checkEmailExists(email);
	}
	
	@Override
	public boolean checkIdExists(String userId) {
		return userMapper.checkIdExists(userId);
	}
	
	@Override
	public List<Group> getGroupList(String userId) {
		return userMapper.getGroupList(userId);
	}
	
	@Override
	public GroupProgressResponseDto getGroupProgress(String groupId) {
		return userMapper.getGroupProgress(groupId);
	}
	
	@Override
	public List<String> getMyRoleInGroup(GetMyRoleInGroupRequestDto myRoleRequest) {
		return userMapper.getMyRoleInGroup(myRoleRequest);
	}
	
	@Override
	public Integer getMemberCntInGroup(String groupId) {
		return userMapper.getMemberCntInGroup(groupId);
	}
	
	@Override
	public void updateProfileImage(String userId, String imageUrl) {
		userMapper.updateProfileImage(userId, imageUrl);
	}
	
	@Override
	public List<InvitationResponseDto> getInvitaionInfo(String userId) {
		return userMapper.getInvitationList(userId);
	}
	
	@Override
	public void handleInvitation(HandleInvitationRequestDto handleInvitationRequest) {
		userMapper.handleInvitation(handleInvitationRequest);
	}
}
