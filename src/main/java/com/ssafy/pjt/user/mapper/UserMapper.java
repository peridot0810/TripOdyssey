package com.ssafy.pjt.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.user.entity.User;
import com.ssafy.pjt.user.model.response.UserInfoResponseDto;

@Mapper
public interface UserMapper {
	
	// 유저 조회 
	User findByEmail(String email);
	User findById(String userId);
	UserInfoResponseDto getUserInfo(String userId);
	
	// 유저 추가 
	int insertUser(User user);
	
	// 중복 체크
	boolean checkIdExists(String userId);
	boolean checkEmailExists(String email);
}
