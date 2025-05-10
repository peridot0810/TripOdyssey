package com.ssafy.pjt.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.user.entity.User;

@Mapper
public interface UserMapper {
	User findByEmail(String email);
}
