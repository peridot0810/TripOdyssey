package com.ssafy.pjt.group.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.group.entity.GroupMemberInfo;
import com.ssafy.pjt.group.service.TravelGroupMemberService;
import com.ssafy.pjt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class TravelGroupMemberController {

	private final TravelGroupMemberService memberService;
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
}
