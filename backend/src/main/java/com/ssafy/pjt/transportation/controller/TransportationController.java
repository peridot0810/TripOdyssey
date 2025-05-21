package com.ssafy.pjt.transportation.controller;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.transportation.dto.request.AddTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.request.UpdateTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.response.TransportationResponseDto;
import com.ssafy.pjt.transportation.service.TransportationService;
import com.ssafy.pjt.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transportation/{groupId}")
public class TransportationController {

	private final TransportationService transportationService;
	private final JwtUtil jwtUtil;
	
	
	@Operation(summary="그룹의 교통편 리스트 조회", description="입력받은 그룹의 교통편 리스트를 조회합니다.")
	@ApiResponse(responseCode = "200", description="교통편 리스트 조회 성공")
	@GetMapping("/list")
	public ResponseEntity<?> getTransportationList(
			@AuthenticationPrincipal UserDetails userDetails, 
			@PathVariable Integer groupId){
		
		String userId = userDetails.getUsername();
		
		List<TransportationResponseDto> transportationList = transportationService.getTransportationList(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "교통편 리스트 조회에 성공했습니다.", transportationList));
	}
	
	@Operation(summary="특정 교통편 조회", description="특정 교통편의 아이디로 교통편을 조회합니다.")
	@ApiResponse(responseCode = "200", description="교통편 조회 성공")
	@GetMapping
	public ResponseEntity<?> getTransportation(
			@AuthenticationPrincipal UserDetails userDetails,  
			@PathVariable Integer groupId, 
			@RequestParam Integer transportationId){

		String userId = userDetails.getUsername();
		
		TransportationResponseDto transportation = transportationService.getTransportation(userId, groupId, transportationId);
		return ResponseEntity.ok(new CommonResponse<>(true, "교통편 조회에 성공했습니다.", transportation));
	}

	@Operation(summary="교통편 추가", description="교통편을 추가합니다. 교통편 담당자만 추가 가능합니다.")
	@ApiResponse(responseCode = "200", description="교통편 추가 성공")
	@PostMapping
	public ResponseEntity<?> addTransportation(
			@AuthenticationPrincipal UserDetails userDetails,  
			@PathVariable Integer groupId, 
			@Valid @RequestBody AddTransportationRequestDto requestDto){

		String userId = userDetails.getUsername();
		
		Integer newTransportationId = transportationService.addTransportation(userId, groupId, requestDto);
		return ResponseEntity.ok(new CommonResponse<>(true, "교통편 추가에 성공했습니다.", Map.of("transportationId", newTransportationId)));
	}

	@Operation(summary="교통편 수정", description="입력받은 아이디의 교통편을 수정합니다.")
	@ApiResponse(responseCode = "200", description="교통편 수정 성공")
	@PutMapping
	public ResponseEntity<?> updateTransportation(
			@AuthenticationPrincipal UserDetails userDetails,  
			@PathVariable Integer groupId, 
			@Valid @RequestBody UpdateTransportationRequestDto requestDto, 
			@RequestParam Integer transportationId){

		String userId = userDetails.getUsername();
		
		requestDto.setTransportationId(transportationId);
		transportationService.updateTransportation(userId, groupId, requestDto);
		return ResponseEntity.ok(new CommonResponse<>(true, "교통편 수정에 성공했습니다.", null));
	}

	@Operation(summary="교통편 삭제", description="입력받은 아이디의 교통편을 삭제합니다.")
	@ApiResponse(responseCode = "200", description="교통편 삭제 성공")
	@DeleteMapping
	public ResponseEntity<?> deleteTransportation(
			@AuthenticationPrincipal UserDetails userDetails, 
			@PathVariable Integer groupId, 
			@RequestParam Integer transportationId){

		String userId = userDetails.getUsername();
		
		transportationService.deleteTransportation(userId, groupId, transportationId);
		return ResponseEntity.ok(new CommonResponse<>(true, "교통편 삭제에 성공했습니다.", null));
	}
}
