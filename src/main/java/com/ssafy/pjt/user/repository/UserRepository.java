package com.ssafy.pjt.user.repository;

import com.ssafy.pjt.user.entity.User;

public interface UserRepository {

	public User findByEmail(String email);
	public int insertUser(User user);
}
