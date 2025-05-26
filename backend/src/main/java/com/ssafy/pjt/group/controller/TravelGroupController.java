package com.ssafy.pjt.group.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.exception.FileUploadIllegalArgumentException;
import com.ssafy.pjt.common.exception.UserNotInGroupException;
import com.ssafy.pjt.common.service.UserValidationService;
import com.ssafy.pjt.group.dto.request.TravelGroupPostRequest;
import com.ssafy.pjt.group.dto.request.TravelGroupUpdateRequest;
import com.ssafy.pjt.group.dto.request.UpdateProgressRequestDto;
import com.ssafy.pjt.group.dto.response.TravelGroupInfoResponse;
import com.ssafy.pjt.group.dto.response.TravelGroupPostResponse;
import com.ssafy.pjt.group.service.TravelGroupService;
import com.ssafy.pjt.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class TravelGroupController {
	
	private final TravelGroupService travelGroupService;
	private final UserValidationService userValidationService;
	
	@PostMapping
	@Operation(summary = "여행 그룹 생성", description = "새로운 여행 그룹을 생성합니다.")
	@ApiResponse(responseCode = "200", description = "여행 그룹 생성 성공")
	public ResponseEntity<CommonResponse<TravelGroupPostResponse>> createTravelGroup(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody TravelGroupPostRequest travelGroupPostRequest){
		String userId = userDetails.getUsername();
		
		CommonResponse<TravelGroupPostResponse> responseData = travelGroupService.createTravelGroup(travelGroupPostRequest, userId);
		
		return ResponseEntity.ok(responseData);
	}
	
	@PutMapping("/{groupId}")
	@Operation(summary = "여행 그룹 수정", description = "여행 그룹의 정보를 수정합니다. MASTER 권한 필요.")
	@ApiResponse(responseCode = "200", description = "여행 그룹 수정 성공 또는 권한 오류 반환")
	public ResponseEntity<CommonResponse<Void>> updateTravelGroup(
			@RequestBody TravelGroupUpdateRequest updateRequest,
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId){
		String userId = userDetails.getUsername();
		
		boolean isUserMaster = userValidationService.isUserRoleValid(userId, groupId, MemberRole.MASTER.getId());
		
		if(isUserMaster) {
			CommonResponse<Void> responseData = travelGroupService.updateTravelGroup(updateRequest);
			return ResponseEntity.ok(responseData);
		} else {
			return ResponseEntity.ok(new CommonResponse<Void>(false, "해당 그룹 변경 권한이 없습니다", null));
		}
	}
	
	@GetMapping("/{groupId}")
	@Operation(summary = "여행 그룹 정보 조회", description = "여행 그룹의 정보를 조회합니다. 그룹원만 접근 가능합니다.")
	@ApiResponse(responseCode = "200", description = "그룹 정보 조회 성공 또는 권한 오류 반환")
	public ResponseEntity<?> getTravelGroupInfo(
			@PathVariable Integer groupId,
			@AuthenticationPrincipal UserDetails userDetails){
		String userId = userDetails.getUsername();
		boolean isUserInGroup = userValidationService.isUserInGroup(userId, groupId);
		if(isUserInGroup) {
			CommonResponse<TravelGroupInfoResponse> responseData = travelGroupService.getTravelGroupInfo(groupId);
			return ResponseEntity.ok(responseData);
		} else {
		    return ResponseEntity.ok(new CommonResponse<Void>(false, "해당 아이디의 그룹이 없거나 그룹원이 아닙니다", null));	
		}
	}
	
	@PostMapping(value="/{groupId}/upload-group-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "그룹 이미지 업로드", description = "그룹 대표 이미지를 업로드합니다. MASTER 또는 MANAGER만 가능.")
	@ApiResponse(responseCode = "200", description = "그룹 이미지 업로드 성공 또는 예외 처리 메시지 반환")
	public ResponseEntity<?> updateGroupImage(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId,
			@RequestParam("file") MultipartFile file){
		
		String userId = userDetails.getUsername();
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.MASTER.getId())) {
			throw new UserNotInGroupException("그룹 이미지는 방장만 변경 가능합니다.");
		}
		
		if(file == null || file.isEmpty()) {
			throw new FileUploadIllegalArgumentException("파일이 없습니다.");
		}
		
		try {
			String imageUrl = travelGroupService.uploadImage(groupId, file);
			return ResponseEntity.ok(new CommonResponse<>(true, "그룹 이미지 업로드에 성공했습니다.", imageUrl));
			
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponse<>(false, "파일 업로드에 실패했습니다.", null));
		}
	}
	
	
	@PutMapping("/{groupId}/update-progress")
	@Operation(summary = "그룹 진척도 업데이트", description = "현재 그룹 진척도를 업데이트합니다.")
	@ApiResponse(responseCode = "200", description = "그룹 진척도 업데이트 완료")
	public ResponseEntity<?> updateProgress(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId,
			@RequestBody UpdateProgressRequestDto updateProgressRequest){
		
		String userId = userDetails.getUsername();
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.MASTER.getId())) {
			throw new UserNotInGroupException("그룹 진척도 업데이트는 방장만 요청 가능합니다.");
		}
		
		travelGroupService.updateProgress(groupId, updateProgressRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "그룹 진척도 업데이트에 성공했습니다.", null));
	}
	
}
