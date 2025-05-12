package com.ssafy.pjt.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;
import com.ssafy.pjt.user.entity.User;

@Mapper
public interface UserMapper {
	
	// 유저 조회 
	User findByEmail(String email);
	User findById(String userId);
	UserInfoResponseDto getUserInfo(String userId);
	
	// 유저 추가 
	int insertUser(User user);
	int insertUserExp(String userId);
	
	// 유저 삭제
	int deleteUser(String userId);
	
	// 유저 활성화/비활성화
	int activateUser(String userId);
	int deactivateUser(String userId);
	
	// 중복 체크
	boolean checkIdExists(String userId);
	boolean checkEmailExists(String email);
}
