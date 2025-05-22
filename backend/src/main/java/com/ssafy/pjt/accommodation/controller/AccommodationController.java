package com.ssafy.pjt.accommodation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.accommodation.dto.request.AddAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.request.UpdateAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.response.GetAccommodationResponseDto;
import com.ssafy.pjt.accommodation.service.AccommodationService;
import com.ssafy.pjt.common.dto.response.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodation/{groupId}")
public class AccommodationController {

	private final AccommodationService accommodationService;

	@Operation(summary = "숙소 추가", description = "숙소 정보를 입력하여 새로운 숙소를 추가합니다.")
	@ApiResponse(responseCode = "200", description = "숙소 추가 성공")
	@PostMapping
	public ResponseEntity<?> addAccommodation(
			@AuthenticationPrincipal UserDetails userDetails,
			@Parameter(description = "그룹 ID", example = "1") @PathVariable Integer groupId,
			@RequestBody AddAccommodationRequestDto addAccommodationRequest) {
		
		String userId = userDetails.getUsername();
		Integer newAccommodationId = accommodationService.addAccommodation(userId, groupId, addAccommodationRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "숙소 추가에 성공했습니다.", Map.of("accommodationId", newAccommodationId)));
	}

	@Operation(summary = "숙소 단건 조회", description = "숙소 ID를 통해 단일 숙소 정보를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "숙소 조회 성공")
	@GetMapping
	public ResponseEntity<?> getAccommodation(
			@AuthenticationPrincipal UserDetails userDetails,
			@Parameter(description = "그룹 ID", example = "1") @PathVariable Integer groupId,
			@Parameter(description = "숙소 ID", example = "10") @RequestParam Integer accommodationId) {
		
		String userId = userDetails.getUsername();
		GetAccommodationResponseDto accommodation = accommodationService.getAccommodation(userId, groupId, accommodationId);
		return ResponseEntity.ok(new CommonResponse<>(true, "숙소 조회에 성공했습니다.", accommodation));
	}

	@Operation(summary = "숙소 목록 조회", description = "그룹 ID를 통해 등록된 모든 숙소를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "숙소 목록 조회 성공")
	@GetMapping("/list")
	public ResponseEntity<?> getAccommodationList(
			@AuthenticationPrincipal UserDetails userDetails,
			@Parameter(description = "그룹 ID", example = "1") @PathVariable Integer groupId) {
		
		String userId = userDetails.getUsername();
		List<GetAccommodationResponseDto> accommodationList = accommodationService.getAccommodationList(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "숙소 조회에 성공했습니다.", accommodationList));
	}

	@Operation(summary = "숙소 삭제", description = "숙소 ID를 통해 숙소 정보를 삭제합니다.")
	@ApiResponse(responseCode = "200", description = "숙소 삭제 성공")
	@DeleteMapping
	public ResponseEntity<?> deleteAccommodation(
			@AuthenticationPrincipal UserDetails userDetails,
			@Parameter(description = "그룹 ID", example = "1") @PathVariable Integer groupId,
			@Parameter(description = "숙소 ID", example = "10") @RequestParam Integer accommodationId) {
		
		String userId = userDetails.getUsername();
		accommodationService.deleteAccommodation(userId, groupId, accommodationId);
		return ResponseEntity.ok(new CommonResponse<>(true, "숙소 삭제에 성공했습니다.", null));
	}

	@Operation(summary = "숙소 정보 수정", description = "숙소 ID를 통해 숙소 정보를 수정합니다.")
	@ApiResponse(responseCode = "200", description = "숙소 수정 성공")
	@PutMapping
	public ResponseEntity<?> updateAccommodation(
			@AuthenticationPrincipal UserDetails userDetails,
			@Parameter(description = "그룹 ID", example = "1") @PathVariable Integer groupId,
			@Parameter(description = "숙소 ID", example = "10") @RequestParam Integer accommodationId,
			@RequestBody UpdateAccommodationRequestDto updateAccommodationRequest) {
		
		String userId = userDetails.getUsername();
		accommodationService.updateAccommodation(userId, groupId, accommodationId, updateAccommodationRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "숙소 업데이트에 성공했습니다.", null));
	}
}
