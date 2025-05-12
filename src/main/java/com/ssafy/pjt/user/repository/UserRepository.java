package com.ssafy.pjt.user.repository;

import java.util.List;

import com.ssafy.pjt.user.dto.request.EditPasswordRequestDto;
import com.ssafy.pjt.user.dto.request.EditUserInfoRequestDto;
import com.ssafy.pjt.user.dto.request.GetMyRoleInGroupRequestDto;
import com.ssafy.pjt.user.dto.response.GroupProgressResponseDto;
import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;
import com.ssafy.pjt.user.entity.Group;
import com.ssafy.pjt.user.entity.User;

public interface UserRepository {

	// 유저 조회 
	public User findByEmail(String email);
	public User findById(String userId);
	public UserInfoResponseDto getUserInfo(String userId);
	
	// 유저 추가 
	public int insertUser(User user);
	public int insertUserExp(String userId);
	
	// 유저 삭제
	public int deleteUser(String userId);
	
	// 유저 정보 수정
	public int editPassword(EditPasswordRequestDto editPasswordRequest);
	public int editUserInfo(EditUserInfoRequestDto editUserInfoRequest);
	
	// 유저 활성화/비활성화
	public int activateUser(String userId);
	public int deactivateUSer(String userId);
	
	// 중복 체크
	public boolean checkIdExists(String userId);
	public boolean checkEmailExists(String email);
	
	// 그룹 조회
	public List<Group> getGroupList(String userId);
	public GroupProgressResponseDto getGroupProgress(String groupId);
	public String getMyRoleInGroup(GetMyRoleInGroupRequestDto myRoleRequest);
	public Integer getMemberCntInGroup(String groupId);
}
