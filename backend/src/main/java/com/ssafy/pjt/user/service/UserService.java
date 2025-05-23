package com.ssafy.pjt.user.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pjt.user.dto.request.EditPasswordRequestDto;
import com.ssafy.pjt.user.dto.request.EditUserInfoRequestDto;
import com.ssafy.pjt.user.dto.request.LoginRequestDto;
import com.ssafy.pjt.user.dto.request.SignUpRequestDto;
import com.ssafy.pjt.user.dto.response.GroupResponseDto;
import com.ssafy.pjt.user.dto.response.LoginResponseDto;
import com.ssafy.pjt.user.dto.response.SearchUserResponseDto;
import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;
import com.ssafy.pjt.user.entity.Group;

public interface UserService {

	// 로그인/회원가입 
	public LoginResponseDto login(LoginRequestDto loginUser);
	public void signup(SignUpRequestDto signupUser);
	
	// 유저 정보 조회 
	public UserInfoResponseDto getUserInfo(String userId);
	public SearchUserResponseDto searchUserByEmail(String email);
	
	// 유저 정보 수정
	public void editUserPassword(EditPasswordRequestDto editPasswordRequest);
	public void editUserInfo(EditUserInfoRequestDto editUserInfoRequest);
	
	// 유저 활성화/비활성화
	public void activateUser(String userId);
	public void deactivateUser(String userId);
	
	// 중복 체크
	public void isEmailDuplicated(String email);
	public void isIdDuplicated(String userId);
	
	// 그룹 조회
	public List<GroupResponseDto> getGroupList(String userId);
	
	// 프로필 이미지 업로드
	public String handleProfileImageUpload(String userId, MultipartFile file) throws IOException;
	
	
}
