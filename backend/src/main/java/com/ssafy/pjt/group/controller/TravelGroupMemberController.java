package com.ssafy.pjt.group.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.common.service.UserValidationService;
import com.ssafy.pjt.financial.dto.request.MemberInviteRequestDto;
import com.ssafy.pjt.group.dto.request.RoleRequestDto;
import com.ssafy.pjt.group.entity.GroupMemberInfo;
import com.ssafy.pjt.group.service.TravelGroupMemberService;
import com.ssafy.pjt.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class TravelGroupMemberController {

	private final TravelGroupMemberService memberService;
	private final UserValidationService userValidationService;

	// 1. 그룹원 전체 조회 
	@Operation(summary = "그룹원 전체 조회", description = "전체 그룹원 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "그룹원 목록 조회 성공")
	@GetMapping("/{groupId}/members")
	public ResponseEntity<CommonResponse<List<GroupMemberInfo>>> getAllGroupMembers(
			@PathVariable Integer groupId,
			@AuthenticationPrincipal UserDetails userDetails) {
		String userId = userDetails.getUsername();
		CommonResponse<List<GroupMemberInfo>> response = memberService.getAllMembers(userId, groupId);
		return ResponseEntity.ok(response);
	}

	// 2. 그룹원 초대 -> deprecated
//	@Operation(summary = "이메일로 그룹원 초대", description = "함께할 그룹원을 이메일로 초대합니다.")
//	@ApiResponse(responseCode = "200", description = "그룹원 초대 성공")
//	@PostMapping("/{groupId}/member")
//	public ResponseEntity<CommonResponse<Void>> inviteMember(
//			@PathVariable Integer groupId,
//			@RequestParam String email, // 이메일로 초대
//			@AuthenticationPrincipal UserDetails userDetails) {
//		String inviterId = userDetails.getUsername();
//		CommonResponse<Void> response = memberService.inviteMember(inviterId, groupId, email);
//		return ResponseEntity.ok(response);
//	}

	// 3. 그룹원 강퇴
	@Operation(summary = "그룹원 강퇴", description = "그룹원을 강퇴합니다.")
	@ApiResponse(responseCode = "200", description = "그룹원 강퇴 성공")
	@DeleteMapping("/{groupId}/member/{userId}")
	public ResponseEntity<CommonResponse<Void>> removeMember(
			@PathVariable Integer groupId,
			@PathVariable String userId,
			@AuthenticationPrincipal UserDetails userDetails) {
		String requesterId = userDetails.getUsername();
		CommonResponse<Void> response = memberService.removeMember(requesterId, groupId, userId);
		return ResponseEntity.ok(response);
	}

	// 4. 역할 임명
	@Operation(summary = "그룹원 역할 임명", description = "그룹원의 역할을 임명합니다.")
	@ApiResponse(responseCode = "200", description = "그룹원 역할 임명 성공")
	@PostMapping("/{groupId}/member/{userId}/role")
	public ResponseEntity<CommonResponse<Void>> assignMemberRole(
			@PathVariable Integer groupId,
			@PathVariable String userId,
			@RequestParam Integer roleId,
			@RequestParam Boolean accept,
			@AuthenticationPrincipal UserDetails userDetails) {
		String requesterId = userDetails.getUsername();
		CommonResponse<Void> response = memberService.assignMemberRole(requesterId, groupId, userId, roleId, accept);
		return ResponseEntity.ok(response);
	}
	
	// 5. 그룹원 초대
	@Operation(summary = "그룹원 초대", description = "함께할 그룹원을 초대합니다.")
	@ApiResponse(responseCode = "200", description = "그룹원 초대 성공")
	@PostMapping("/{groupId}/member/invite")
	public ResponseEntity<?> inviteMember(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId,
			@RequestBody MemberInviteRequestDto memberInviteRequest){
		
		String userId = userDetails.getUsername();
		
		memberService.memberInvite(groupId, userId, memberInviteRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "그룹원 초대에 성공했습니다.", null));
	}
	
	// 6. 초대한 사람 목록 조회
	@Operation(summary = "초대한 그룹원 목록 조회", description = "초대한 멤버 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "초대 목록 조회 성공")
	@GetMapping("/{groupId}/member/invited-list")
	public ResponseEntity<?> getInvitedList(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId){
		
		String userId = userDetails.getUsername();
		List<?> invitedList = memberService.getInvitedMemberList(groupId, userId);
		return ResponseEntity.ok(new CommonResponse<>(true, "초대 목록 조회에 성공했습니다.", invitedList));
	}
	
	// 7. 그룹원의 역할 목록 조회
	@Operation(summary = "유저의 역할 목록 조회", description = "유저의 해당 그룹에서의 역할 목록을 boolean 배열 형태로 반환합니다.")
	@ApiResponse(responseCode = "200", description = "유저의 역할 목록 조회 성공")
	@GetMapping("/{groupId}/member/roles")
	public ResponseEntity<?> getMemberRoles(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId){
		
		String userId = userDetails.getUsername();
		Boolean[] myRoles = userValidationService.getUserRoles(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "유저 역할 목록 조회에 성공했습니다.", myRoles));
	}
	
	// 8. 그룹에 희망 역할 요청
	@Operation(summary = "희망하는 역할 요청", description = "유저가 희망하는 역할을 요청합니다.")
	@ApiResponse(responseCode = "200", description = "희망 역할 요청 성공")
	@PostMapping("/{groupId}/member/role-request")
	public ResponseEntity<?> requestRole(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId,
			@RequestBody RoleRequestDto roleRequest){
		
		String userId = userDetails.getUsername();
		memberService.roleRequest(groupId, userId, roleRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "역할 신청에 성공했습니다.", null));
	}
	
	
	
	// 9. 역할 요청 목록 조회
	@Operation(summary = "역할 요청 목록 조회", description = "현재 역할 요청 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "역할 요청 목록 조회 성공")
	@GetMapping("/{groupId}/member/role-request")
	public ResponseEntity<?> requestRole(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId){
		
		String userId = userDetails.getUsername();
		List<?> roleRequestList = memberService.getRoleRequestList(groupId, userId);
		return ResponseEntity.ok(new CommonResponse<>(true, "역할 신청 목록 조회에 성공했습니다.", roleRequestList));
	}
}
