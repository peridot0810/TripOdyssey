package com.ssafy.pjt.user.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.ssafy.pjt.user.entity.User;
import com.ssafy.pjt.user.exception.DuplicateUserException;
import com.ssafy.pjt.user.exception.GetUserInfoFailedException;
import com.ssafy.pjt.user.exception.LoginFailedException;
import com.ssafy.pjt.user.model.request.LoginRequestDto;
import com.ssafy.pjt.user.model.request.SignUpRequestDto;
import com.ssafy.pjt.user.model.response.LoginResponseDto;
import com.ssafy.pjt.user.model.response.UserInfoResponseDto;
import com.ssafy.pjt.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
		throw new LoginFailedException("로그인에 실패했습니다.");
	}
	
	@Override
	public void signup(SignUpRequestDto signupUser) {
		
		User user = User.builder()
				.id(signupUser.getId())
				.email(signupUser.getEmail())
				.pw(signupUser.getPw())
				.nickname(signupUser.getNickname())
				.gender(signupUser.getGender())
				.age(signupUser.getAge())
				.build();
		
		// 비밀번호 암호화 로직 추가 필요 
		
		try {
			userRepository.insertUser(user);
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateUserException("이미 사용 중인 아이디 또는 이메일 입니다.");
		}
	}
	
	@Override
	public UserInfoResponseDto getUserInfo(String userId) {
		
		log.debug("targetId : {}", userId);
		
		UserInfoResponseDto userInfo = userRepository.getUserInfo(userId);
		if(userInfo != null) {
			// 유저 정보 조회 성공  
			return userInfo;
		}
		// 유저 정보 조회 실패
		throw new GetUserInfoFailedException("존재하지 않는 유저입니다.");
		
	}
}
