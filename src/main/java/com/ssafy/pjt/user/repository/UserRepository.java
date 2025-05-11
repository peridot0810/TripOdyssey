package com.ssafy.pjt.user.repository;

import com.ssafy.pjt.user.entity.User;
import com.ssafy.pjt.user.model.response.UserInfoResponseDto;

public interface UserRepository {

	public User findByEmail(String email);
	public User findById(String userId);
	public UserInfoResponseDto getUserInfo(String userId);
	public int insertUser(User user);
}
