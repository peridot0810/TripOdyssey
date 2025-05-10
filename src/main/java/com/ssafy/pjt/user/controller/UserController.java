package com.ssafy.pjt.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.user.model.request.LoginRequestDto;
import com.ssafy.pjt.user.model.request.SignUpRequestDto;
import com.ssafy.pjt.user.model.response.LoginResponseDto;
import com.ssafy.pjt.user.service.UserService;
import com.ssafy.pjt.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final JwtUtil jwtUtil;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest){
		
		LoginResponseDto loginResponse = userService.login(loginRequest);
		
		// JWT 발급
		String token = jwtUtil.createToken(loginResponse.getId());
		
		// 응답에 JWT 토큰 포함
		return ResponseEntity.ok()
				.header("Authorization", "Bearer " + token)
				.body(loginResponse);	
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequestDto signupRequest){
		
		userService.signup(signupRequest);
	    return ResponseEntity.ok("회원가입 성공");
	}

}
