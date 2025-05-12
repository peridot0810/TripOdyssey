package com.ssafy.pjt.user.repository;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;
import com.ssafy.pjt.user.entity.User;
import com.ssafy.pjt.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

	private final UserMapper userMapper;
	
	@Override
	public User findByEmail(String email) {
		return userMapper.findByEmail(email);
	}
	
	@Override
	public User findById(String userId) {
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
	public boolean checkEmailExists(String email) {
		return userMapper.checkEmailExists(email);
	}
	
	@Override
	public boolean checkIdExists(String userId) {
		return userMapper.checkIdExists(userId);
	}
}
