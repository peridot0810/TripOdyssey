package com.ssafy.pjt.user.service;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.user.entity.User;
import com.ssafy.pjt.user.model.request.LoginRequestDto;
import com.ssafy.pjt.user.model.response.LoginResponseDto;
import com.ssafy.pjt.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	
	@Override
	public LoginResponseDto login(LoginRequestDto loginUser) {
		
		User userInfo = userRepository.findByEmail(loginUser.getEmail());
		
		if(userInfo != null && userInfo.getPw().equals(loginUser.getPw())) {
			// 로그인 성공 
			return LoginResponseDto.builder()
					.id(userInfo.getId())
					.email(userInfo.getEmail())
					.gender(userInfo.getGender())
					.nickname(userInfo.getNickname())
					.age(userInfo.getAge())
					.build();
		}
		// 로그인 실패
		return null;
	}
}
