package com.ssafy.pjt.user.service;

import com.ssafy.pjt.user.model.request.LoginRequestDto;
import com.ssafy.pjt.user.model.response.LoginResponseDto;

public interface UserService {

	public LoginResponseDto login(LoginRequestDto loginUser);
}
