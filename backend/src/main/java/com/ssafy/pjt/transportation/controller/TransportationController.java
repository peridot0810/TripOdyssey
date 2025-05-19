package com.ssafy.pjt.transportation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transportation/{groupId}")
public class TransportationController {

	private final TransportationService transportationService;
	private final JwtUtil jwtUtil;
	
	@GetMapping("/list")
	public ResponseEntity<?> getTransportationList(@RequestHeader("Authorization") String token, @PathVariable Integer groupId){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		List<TransportationResponseDto> transportationList = transportationService.getTransportationList(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "교통편 리스트 조회에 성공했습니다.", transportationList));
	}
	
	@GetMapping
	public ResponseEntity<?> getTransportation(@RequestHeader("Authorization") String token, @PathVariable Integer groupId, @RequestParam Integer transportationId){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		TransportationResponseDto transportation = transportationService.getTransportation(userId, groupId, transportationId);
		return ResponseEntity.ok(new CommonResponse<>(true, "교통편 조회에 성공했습니다.", transportation));
	}

	@PostMapping
	public ResponseEntity<?> addTransportation(@RequestHeader("Authorization") String token, @PathVariable Integer groupId, @RequestBody AddTransportationRequestDto requestDto){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		transportationService.addTransportation(userId, groupId, requestDto);
		return ResponseEntity.ok(new CommonResponse<>(true, "교통편 추가에 성공했습니다.", null));
	}

	@PutMapping
	public ResponseEntity<?> updateTransportation(@RequestHeader("Authorization") String token, @PathVariable Integer groupId, @RequestBody UpdateTransportationRequestDto requestDto, @RequestParam Integer transportationId){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		requestDto.setTransportationId(transportationId);
		transportationService.updateTransportation(userId, groupId, requestDto);
		return ResponseEntity.ok(new CommonResponse<>(true, "교통편 수정에 성공했습니다.", null));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteTransportation(@RequestHeader("Authorization") String token, @PathVariable Integer groupId, @RequestParam Integer transportationId){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		transportationService.deleteTransportation(userId, groupId, transportationId);
		return ResponseEntity.ok(new CommonResponse<>(true, "교통편 삭제에 성공했습니다.", null));
	}
}
