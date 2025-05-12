package com.ssafy.pjt.user.service;

import com.ssafy.pjt.user.dto.request.LoginRequestDto;
import com.ssafy.pjt.user.dto.request.SignUpRequestDto;
import com.ssafy.pjt.user.dto.response.LoginResponseDto;
import com.ssafy.pjt.user.dto.response.SearchUserResponseDto;
import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;

public interface UserService {

	// 로그인/회원가입 
	public LoginResponseDto login(LoginRequestDto loginUser);
	public void signup(SignUpRequestDto signupUser);
	
	// 유저 정보 조회 
	public UserInfoResponseDto getUserInfo(String userId);
	public SearchUserResponseDto searchUserByEmail(String email);
	
	// 중복 체크
	public void isEmailDuplicated(String email);
	public void isIdDuplicated(String userId);
	
}
