package com.ssafy.pjt.user.service;

import com.ssafy.pjt.user.model.request.LoginRequestDto;
import com.ssafy.pjt.user.model.request.SignUpRequestDto;
import com.ssafy.pjt.user.model.response.LoginResponseDto;
import com.ssafy.pjt.user.model.response.UserInfoResponseDto;

public interface UserService {

	public LoginResponseDto login(LoginRequestDto loginUser);
	public UserInfoResponseDto getUserInfo(String userId);
	public void signup(SignUpRequestDto signupUser);
}
