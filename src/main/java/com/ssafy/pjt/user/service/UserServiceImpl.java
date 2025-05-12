package com.ssafy.pjt.user.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.ssafy.pjt.user.dto.request.EditPasswordRequestDto;
import com.ssafy.pjt.user.dto.request.EditUserInfoRequestDto;
import com.ssafy.pjt.user.dto.request.LoginRequestDto;
import com.ssafy.pjt.user.dto.request.SignUpRequestDto;
import com.ssafy.pjt.user.dto.response.GroupProgressResponseDto;
import com.ssafy.pjt.user.dto.response.GroupResponseDto;
import com.ssafy.pjt.user.dto.response.LoginResponseDto;
import com.ssafy.pjt.user.dto.response.SearchUserResponseDto;
import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;
import com.ssafy.pjt.user.entity.Group;
import com.ssafy.pjt.user.entity.User;
import com.ssafy.pjt.user.exception.DuplicateUserException;
import com.ssafy.pjt.user.exception.EditUserInfoFailedException;
import com.ssafy.pjt.user.exception.GetUserInfoFailedException;
import com.ssafy.pjt.user.exception.LoginFailedException;
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
		
		if(userInfo != null && userInfo.getPw().equals(loginUser.getPw()) && userInfo.getActivated()) {
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
			userRepository.insertUserExp(user.getId());
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateUserException("이미 사용 중인 아이디 또는 이메일 입니다.");
		}
	}
	
	@Override
	public UserInfoResponseDto getUserInfo(String userId) {
		UserInfoResponseDto userInfo = userRepository.getUserInfo(userId);
		if(userInfo != null) {
			// 유저 정보 조회 성공  
			return userInfo;
		}
		// 유저 정보 조회 실패
		throw new GetUserInfoFailedException("존재하지 않는 사용자 입니다.");
	}
	
	@Override
	public SearchUserResponseDto searchUserByEmail(String email) {
		User targetUser = userRepository.findByEmail(email);
		if(targetUser != null && targetUser.getActivated()) {
			// 검색 유저가 존재 
			return SearchUserResponseDto.builder()
					.id(targetUser.getId())
					.nickname(targetUser.getNickname())
					.build();
		}
		// 검색 유저가 존재하지 않음 
		throw new GetUserInfoFailedException("존재하지 않는 사용자 입니다.");
	}
	
	@Override
	public void editUserPassword(EditPasswordRequestDto editPasswordRequest) {
		User targetUser = userRepository.findById(editPasswordRequest.getId());
		
		if(targetUser != null && targetUser.getPw().equals(editPasswordRequest.getCurrentPassword())) {
			// 비밀번호를 수정할 유저가 존재 + 비밀번호 일치
			userRepository.editPassword(editPasswordRequest);
			return;
		}
		// 수정 조건 미달성
		throw new EditUserInfoFailedException("계정이 존재하지 않거나, 비밀번호가 틀렸습니다.");
	}
	
	@Override
	public void editUserInfo(EditUserInfoRequestDto editUserInfoRequest) {
		if(editUserInfoRequest.getNewNickname() == null && editUserInfoRequest.getNewAge() == null) {
			// 아무 값도 없다면 예외 던지기
			throw new EditUserInfoFailedException("변경할 값이 없습니다.");
		}
		userRepository.editUserInfo(editUserInfoRequest);
	}
	
	@Override
	public void activateUser(String userId) {
		userRepository.activateUser(userId);
	}
	
	@Override
	public void deactivateUser(String userId) {
		userRepository.deactivateUSer(userId);
	}
	
	@Override
	public void isEmailDuplicated(String email) {
		if(userRepository.checkEmailExists(email)) {
			throw new DuplicateUserException("이미 존재하는 이메일 입니다.");
		}
	}
	
	@Override
	public void isIdDuplicated(String userId) {
		if(userRepository.checkIdExists(userId)) {
			throw new DuplicateUserException("이미 존재하는 아이디 입니다.");
		}
	}
	
	@Override
	public List<GroupResponseDto> getGroupList(String userId) {
		List<Group> groupList = userRepository.getGroupList(userId); 
		List<GroupResponseDto> retList = new ArrayList<>();
		for(Group group : groupList) {
			GroupProgressResponseDto progress = userRepository.getGroupProgress(group.getGroupId());
			
			retList.add(GroupResponseDto.builder()
					.groupId(group.getGroupId())
					.name(group.getName())
					.status(group.getStatus())
					.startDate(group.getStartDate())
					.endDate(group.getEndDate())
					.progress(progress)
					.build()
					);
		}
		
		return retList;
	}
}
