package com.ssafy.pjt.group.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.service.UserValidationService;
import com.ssafy.pjt.group.dto.request.TravelGroupPostRequest;
import com.ssafy.pjt.group.dto.request.TravelGroupUpdateRequest;
import com.ssafy.pjt.group.dto.response.TravelGroupInfoResponse;
import com.ssafy.pjt.group.dto.response.TravelGroupPostResponse;
import com.ssafy.pjt.group.service.TravelGroupService;
import com.ssafy.pjt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class TravelGroupController {
	
	private final TravelGroupService travelGroupService;
	private final UserValidationService userValidationService;
	
	private final JwtUtil jwtUtil;
	
	@PostMapping
	public ResponseEntity<CommonResponse<TravelGroupPostResponse>> createTravelGroup(
			@RequestHeader("Authorization") String token,
			@RequestBody TravelGroupPostRequest travelGroupPostRequest){
		String userId = jwtUtil.extractUserId(token);
		
		CommonResponse<TravelGroupPostResponse> responseData = travelGroupService.createTravelGroup(travelGroupPostRequest, userId);
		
		return ResponseEntity.ok(responseData);
	}
	
	@PutMapping("/{groupId}")
	public ResponseEntity<CommonResponse<Void>> updateTravelGroup(
			@RequestBody TravelGroupUpdateRequest updateRequest,
			@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId){
		String userId = jwtUtil.extractUserId(token);
		
		boolean isUserMaster = userValidationService.isUserRoleValid(userId, groupId, MemberRole.MASTER.getId());
		
		if(isUserMaster) {
			CommonResponse<Void> responseData = travelGroupService.updateTravelGroup(updateRequest);
			return ResponseEntity.ok(responseData);
		} else {
			return ResponseEntity.ok(new CommonResponse<Void>(false, "해당 그룹 변경 권한이 없습니다", null));
		}
	}
	
	@GetMapping("/{groupId}")
	public ResponseEntity<?> getTravelGroupInfo(
			@PathVariable Integer groupId,
			@RequestHeader("Authorization") String token){
		String userId = jwtUtil.extractUserId(token);
		boolean isUserInGroup = userValidationService.isUserInGroup(userId, groupId);
		if(isUserInGroup) {
			CommonResponse<TravelGroupInfoResponse> responseData = travelGroupService.getTravelGroupInfo(groupId);
			return ResponseEntity.ok(responseData);
		} else {
		    return ResponseEntity.ok(new CommonResponse<Void>(false, "해당 아이디의 그룹이 없거나 그룹원이 아닙니다", null));	
		}
	}
	
}
