package com.ssafy.pjt.user.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.user.dto.request.EditPasswordRequestDto;
import com.ssafy.pjt.user.dto.request.EditUserInfoRequestDto;
import com.ssafy.pjt.user.dto.request.LoginRequestDto;
import com.ssafy.pjt.user.dto.request.SignUpRequestDto;
import com.ssafy.pjt.user.dto.response.GroupResponseDto;
import com.ssafy.pjt.user.dto.response.LoginResponseDto;
import com.ssafy.pjt.user.dto.response.SearchUserResponseDto;
import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;
import com.ssafy.pjt.user.entity.Group;
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
	
	/**
	 * 로그인 
	 * 
	 * @param loginRequest
	 * @return
	 */
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
	
	
	/**
	 * 회원가입 
	 * 
	 * @param signupRequest
	 * @return
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequestDto signupRequest){
		userService.signup(signupRequest);
	    return ResponseEntity.ok("회원가입 성공");
	}
	
	
	/**
	 * 본인 정보 조회 
	 * 
	 * @param token
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> getMyInfo(@RequestHeader("Authorization") String token){
		// JWT 토큰에서 id 추출 
		String myId = jwtUtil.extractUserId(token);
		
		UserInfoResponseDto myInfo = userService.getUserInfo(myId);
		return ResponseEntity.ok(myInfo);
	}
	
	
	/**
	 * 이메일 중복 체크 
	 * 
	 * @param email
	 * @return
	 */
	@GetMapping("/check/email")
	public ResponseEntity<?> checkEmailDuplicate(@RequestParam String email){
		userService.isEmailDuplicated(email);
		return ResponseEntity.ok("사용 가능한 이메일 입니다.");
	}
	
	
	/**
	 * 아이디 중복 체크
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/check/id")
	public ResponseEntity<?> checkIdDuplicate(@RequestParam String id){
		userService.isIdDuplicated(id);
		return ResponseEntity.ok("사용 가능한 아이디 입니다.");
	}
	
	
	/**
	 * 유저 검색
	 * 
	 * @param email
	 * @return
	 */
	@GetMapping("/search")
	public ResponseEntity<?> searchUserByEmail(@RequestParam String email){
		SearchUserResponseDto targetUser = userService.searchUserByEmail(email);
		return ResponseEntity.ok(targetUser);
	}
	
	
	/**
	 * 비밀번호 변경
	 * 
	 * @param token
	 * @param editPasswordRequest
	 * @return
	 */
	@PostMapping("/change/password")
	public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @RequestBody EditPasswordRequestDto editPasswordRequest){
		// JWT 토큰에서 id 추출
		String targetId = jwtUtil.extractUserId(token);
		editPasswordRequest.setId(targetId);
		
		userService.editUserPassword(editPasswordRequest);
		return ResponseEntity.ok("비밀번호 수정에 성공했습니다.");
	}
	
	
	@PutMapping 
	public ResponseEntity<?> changeUserInfo(@RequestHeader("Authorization") String token, @RequestBody EditUserInfoRequestDto editUserInfoRequest){
		// JWT 토큰에서 id 추출
		String targetId = jwtUtil.extractUserId(token);
		editUserInfoRequest.setId(targetId);
		
		userService.editUserInfo(editUserInfoRequest);
		return ResponseEntity.ok("회원 정보 수정에 성공했습니다.");
	}
	
	
	/**
	 * 회원 탈퇴
	 * 
	 * @param token
	 * @return
	 */
	@DeleteMapping
	public ResponseEntity<?> deactivateUser(@RequestHeader("Authorization") String token){
		// JWT 토큰에서 id 추출 
		String myId = jwtUtil.extractUserId(token);
		
		userService.deactivateUser(myId);
		return ResponseEntity.ok("회원 탈퇴 성공");
	}
	
	
	@GetMapping("/groups")
	public ResponseEntity<?> getMyGroupList(@RequestHeader("Authorization") String token){
		// JWT 토큰에서 id 추출 
		String myId = jwtUtil.extractUserId(token);
		
		List<GroupResponseDto> myGroupList = userService.getGroupList(myId);
		return ResponseEntity.ok(myGroupList);
				
	}

}
