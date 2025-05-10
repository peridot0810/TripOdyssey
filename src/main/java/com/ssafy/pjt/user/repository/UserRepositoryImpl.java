package com.ssafy.pjt.user.repository;

import org.springframework.stereotype.Repository;

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
	public int insertUser(User user) {
		return userMapper.insertUser(user);
	}
}
