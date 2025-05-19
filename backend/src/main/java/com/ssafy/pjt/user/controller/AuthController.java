package com.ssafy.pjt.user.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.user.dto.request.LoginRequestDto;
import com.ssafy.pjt.user.dto.request.SignUpRequestDto;
import com.ssafy.pjt.user.dto.response.JwtResponse;
import com.ssafy.pjt.user.dto.response.LoginResponseDto;
import com.ssafy.pjt.user.entity.User;
import com.ssafy.pjt.user.exception.LoginFailedException;
import com.ssafy.pjt.user.service.UserService;
import com.ssafy.pjt.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	
	/**
	 * 로그인 
	 * 
	 * @param loginRequest
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest){
		try {
			// 인증을 위한 Token 생성 (principal, credential)
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPw());
			
			// 인증 시도
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			log.debug("authentication :{}", authentication.toString());
			
			// 인증 성공 후 JWT 생성
			String jwt = jwtUtil.createToken(authentication);
			
			log.debug("jwt : {}", jwt);
			log.debug("userInfo : {}",jwtUtil.extractUserInfo(jwt));
			
			//서비스 메서드에서 LoginResponseDto 가져오기
	        LoginResponseDto userDto = userService.login(loginRequest);
			
			// JWT를 응답으로 반환
			return ResponseEntity.ok(JwtResponse.builder()
					.accessToken(jwt)
					.user(userDto)
					.build()
				);
			
		} catch (Exception e) {
			throw new LoginFailedException("아이디 또는 비밀번호가 올바르지 않습니다.");
		}
	}
	
	
	/**
	 * 회원가입 
	 * 
	 * @param signupRequest
	 * @return
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequestDto signupRequest){
		log.debug("signupRequest : {}", signupRequest);
		
		userService.signup(signupRequest);
	    return ResponseEntity.ok("회원가입 성공");
	}
}
