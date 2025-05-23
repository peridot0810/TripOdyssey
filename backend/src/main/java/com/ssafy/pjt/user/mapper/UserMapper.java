package com.ssafy.pjt.user.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.user.dto.request.EditPasswordRequestDto;
import com.ssafy.pjt.user.dto.request.EditUserInfoRequestDto;
import com.ssafy.pjt.user.dto.request.GetMyRoleInGroupRequestDto;
import com.ssafy.pjt.user.dto.response.GroupProgressResponseDto;
import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;
import com.ssafy.pjt.user.entity.User;
import com.ssafy.pjt.user.entity.Group;

@Mapper
public interface UserMapper {
	
	// 유저 조회 
	Optional<User> findByEmail(String email);
	Optional<User> findById(String userId);
	UserInfoResponseDto getUserInfo(String userId);
	
	// 유저 추가 
	int insertUser(User user);
	int insertUserExp(String userId);
	
	// 유저 삭제
	int deleteUser(String userId);
	
	// 유저 정보 수정
	int editPassword(EditPasswordRequestDto editPasswordRequest);
	int editUserInfo(EditUserInfoRequestDto editUserInfoRequest);
	
	// 유저 활성화/비활성화
	int activateUser(String userId);
	int deactivateUser(String userId);
	
	// 중복 체크
	boolean checkIdExists(String userId);
	boolean checkEmailExists(String email);
	
	// 그룹 조회
	List<Group> getGroupList(String userId);
	GroupProgressResponseDto getGroupProgress(String groupId);
	String getMyRoleInGroup(GetMyRoleInGroupRequestDto myRoleRequest);
	Integer getMemberCntInGroup(String groupId);
	
	// 프로필 이미지
	void updateProfileImage(String userId, String imageUrl);
	
}
