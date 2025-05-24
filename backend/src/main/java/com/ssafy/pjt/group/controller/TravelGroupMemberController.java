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
	private final JwtUtil jwtUtil;

	// 1. 그룹원 전체 조회
	@GetMapping("/{groupId}/members")
	public ResponseEntity<CommonResponse<List<GroupMemberInfo>>> getAllGroupMembers(
			@PathVariable Integer groupId,
			@RequestHeader("Authorization") String token) {
		String userId = jwtUtil.extractUserId(token);
		// (optional) check permission here
		CommonResponse<List<GroupMemberInfo>> response = memberService.getAllMembers(groupId);
		return ResponseEntity.ok(response);
	}

	// 2. 그룹원 초대
	@PostMapping("/{groupId}/member")
	public ResponseEntity<CommonResponse<Void>> inviteMember(
			@PathVariable Integer groupId,
			@RequestParam String email, // 이메일로 초대
			@RequestHeader("Authorization") String token) {
		String inviterId = jwtUtil.extractUserId(token);
		// (optional) check inviter permission
		CommonResponse<Void> response = memberService.inviteMember(groupId, email);
		return ResponseEntity.ok(response);
	}

	// 3. 그룹원 강퇴
	@DeleteMapping("/{groupId}/member/{userId}")
	public ResponseEntity<CommonResponse<Void>> removeMember(
			@PathVariable Integer groupId,
			@PathVariable String userId,
			@RequestHeader("Authorization") String token) {
		String requesterId = jwtUtil.extractUserId(token);
		// (optional) check permission
		CommonResponse<Void> response = memberService.removeMember(groupId, userId);
		return ResponseEntity.ok(response);
	}

	// 4. 역할 임명
	@PostMapping("/{groupId}/member/{userId}/role")
	public ResponseEntity<CommonResponse<Void>> assignMemberRole(
			@PathVariable Integer groupId,
			@PathVariable String userId,
			@RequestParam Integer roleId,
			@RequestHeader("Authorization") String token) {
		String requesterId = jwtUtil.extractUserId(token);
		// (optional) check permission
		CommonResponse<Void> response = memberService.assignMemberRole(groupId, userId, roleId);
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
		
		// 본인을 초대할 수는 없음
		if(userId.equals(memberInviteRequest.getReceiverId())) {
			return ResponseEntity.badRequest().body(new CommonResponse<>(false, "자기 자신을 초대할 수는 없습니다.", null));
		}
		
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
	@Operation(summary = "요청 유저의 역할 목록 조회", description = "유저의 해당 그룹에서의 역할 목록을 boolean 배열 형태로 반환합니다.")
	@ApiResponse(responseCode = "200", description = "유저의 역할 목록 조회 성공")
	@GetMapping("/{groupId}/member/roles")
	public ResponseEntity<?> getMemberRoles(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId){
		
		String userId = userDetails.getUsername();
		boolean[] myRoles = userValidationService.getUserRoles(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "유저 역할 목록 조회에 성공했습니다.", myRoles));
	}
	
}
