package com.ssafy.pjt.user.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.user.dto.request.EditPasswordRequestDto;
import com.ssafy.pjt.user.dto.request.EditUserInfoRequestDto;
import com.ssafy.pjt.user.dto.request.HandleInvitationRequestDto;
import com.ssafy.pjt.user.dto.response.GroupResponseDto;
import com.ssafy.pjt.user.dto.response.InvitationResponseDto;
import com.ssafy.pjt.user.dto.response.SearchUserResponseDto;
import com.ssafy.pjt.user.dto.response.UserInfoResponseDto;
import com.ssafy.pjt.user.entity.CustomUserDetails;
import com.ssafy.pjt.user.service.UserService;
import com.ssafy.pjt.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final JwtUtil jwtUtil;
	
	@Operation(summary="사용자 정보 조회", description="사용자 아이디로 사용자 정보를 조회합니다.")
	@ApiResponse(responseCode = "200", description="사용자 정보 조회 성공")
	@GetMapping
	public ResponseEntity<?> getMyInfo(
			@AuthenticationPrincipal UserDetails userDetails
			){		
		UserInfoResponseDto myInfo = userService.getUserInfo(userDetails.getUsername());
		return ResponseEntity.ok(myInfo);
	}
	
	
	@Operation(summary="이메일 중복 체크", description="사용 가능한 이메일인지 확인합니다.")
	@ApiResponse(responseCode = "200", description="사용 가능한 이메일")
	@GetMapping("/check/email")
	public ResponseEntity<?> checkEmailDuplicate(@RequestParam String email){
		userService.isEmailDuplicated(email);
		return ResponseEntity.ok("사용 가능한 이메일 입니다.");
	}
	
	
	@Operation(summary="아이디 중복 체크", description="사용 가능한 아이디인지 확인합니다.")
	@ApiResponse(responseCode = "200", description="사용 가능한 아이디")
	@GetMapping("/check/id")
	public ResponseEntity<?> checkIdDuplicate(@RequestParam String id){
		userService.isIdDuplicated(id);
		return ResponseEntity.ok("사용 가능한 아이디 입니다.");
	}
	
	
	@Operation(summary="사용자 검색", description="이메일 기반으로 사용자 정보(아이디, 닉네임)를 조회합니다.")
	@ApiResponse(responseCode = "200", description="사용자 조회 성공")
	@GetMapping("/search")
	public ResponseEntity<?> searchUserByEmail(@RequestParam String email){
		SearchUserResponseDto targetUser = userService.searchUserByEmail(email);
		return ResponseEntity.ok(targetUser);
	}
	
	
	@Operation(summary="사용자 비밀번호 변경", description="사용자의 비밀번호를 변경합니다.")
	@ApiResponse(responseCode = "200", description="비밀번호 변경 성공")
	@PostMapping("/change/password")
	public ResponseEntity<?> changePassword(
			@AuthenticationPrincipal UserDetails userDetails, 
			@Valid @RequestBody EditPasswordRequestDto editPasswordRequest){
		String targetId = userDetails.getUsername();
		editPasswordRequest.setId(targetId);
		
		userService.editUserPassword(editPasswordRequest);
		return ResponseEntity.ok("비밀번호 수정에 성공했습니다.");
	}
	
	@Operation(summary="사용자 정보 수정", description="사용자의 정보를 수정합니다.")
	@ApiResponse(responseCode = "200", description="사용자 정보 수정 성공")
	@PutMapping 
	public ResponseEntity<?> changeUserInfo(
			@AuthenticationPrincipal UserDetails userDetails, 
			@Valid @RequestBody EditUserInfoRequestDto editUserInfoRequest){
		String targetId = userDetails.getUsername();
		editUserInfoRequest.setId(targetId);
		
		userService.editUserInfo(editUserInfoRequest);
		return ResponseEntity.ok("회원 정보 수정에 성공했습니다.");
	}
	
	
	@Operation(summary="사용자 탈퇴", description="사용자를 비활성화 상태로 변경합니다.")
	@ApiResponse(responseCode = "200", description="사용자 탈퇴 성공")
	@DeleteMapping
	public ResponseEntity<?> deactivateUser(@AuthenticationPrincipal UserDetails userDetails){
		String myId = userDetails.getUsername();
		
		userService.deactivateUser(myId);
		return ResponseEntity.ok("회원 탈퇴 성공");
	}
	
	
	@Operation(summary="사용자 그룹 조회", description="사용자가 속한 그룹 리스트를 반환합니다.")
	@ApiResponse(responseCode = "200", description="사용자 그룹 조회 성공")
	@GetMapping("/groups")
	public ResponseEntity<?> getMyGroupList(@AuthenticationPrincipal UserDetails userDetails){
		String myId = userDetails.getUsername();
		
		List<GroupResponseDto> myGroupList = userService.getGroupList(myId);
		return ResponseEntity.ok(myGroupList);
	}
	
	
	@Operation(summary="사용자 프로필 이미지 업로드", description="사용자의 프로필 이미지를 업로드합니다.")
	@ApiResponse(responseCode = "200", description="사용자 프로필 이미지 업로드 성공")
	@PostMapping(value="/upload-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadProfileImage(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam("file") MultipartFile file){
		
		String userId = userDetails.getUsername();
		
		if(file==null || file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일이 없습니다.");
		}
		
		try {
			String newImageUrl = userService.handleProfileImageUpload(userId, file);
			return ResponseEntity.ok(new CommonResponse<>(true, "프로필 이미지 업로드에 성공했습니다.", Map.of("imageUrl", newImageUrl)));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드에 실패했습니다. : " + e.getMessage());
		}
	}
	
	
	@Operation(summary="초대 받은 데이터 조회", description="받은 초대 건수들을 조회합니다.")
	@ApiResponse(responseCode = "200", description="초대 데이터 조회에 성공했습니다.")
	@GetMapping("/invited")
	public ResponseEntity<?> getInvitedInfo(
			@AuthenticationPrincipal UserDetails userDetails){
		
		String userId = userDetails.getUsername();
		List<InvitationResponseDto> invitationInfoList = userService.getInvitationInfo(userId);
		return ResponseEntity.ok(new CommonResponse<>(true, "초대 받은 정보 조회에 성공했습니다.", invitationInfoList));
	}
	
	@Operation(summary="초대 수락/거절", description="초대를 수락 또는 거절합니다.")
	@ApiResponse(responseCode = "200", description="초대 수락/거절에 성공했습니다.")
	@PutMapping("/invited")
	public ResponseEntity<?> handleInvitation(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody HandleInvitationRequestDto requestDto){
		
		String userId = userDetails.getUsername();
		userService.handleInvitation(userId, requestDto);
		return ResponseEntity.ok(new CommonResponse<>(true, "초대 수락/거절에 성공했습니다.", null));
	}

}
