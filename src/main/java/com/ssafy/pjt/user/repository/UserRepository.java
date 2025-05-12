package com.ssafy.pjt.user.repository;

import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;
import com.ssafy.pjt.user.entity.User;

public interface UserRepository {

	// 유저 조회 
	public User findByEmail(String email);
	public User findById(String userId);
	public UserInfoResponseDto getUserInfo(String userId);
	
	// 유저 추가 
	public int insertUser(User user);
	
	// 중복 체크
	public boolean checkIdExists(String userId);
	public boolean checkEmailExists(String email);
}
