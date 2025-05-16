package com.ssafy.pjt.schedule.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;
import com.ssafy.pjt.schedule.service.ScheduleService;
import com.ssafy.pjt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/{groupId}")
public class ScheduleController {

	private final ScheduleService scheduleService;
	private final JwtUtil jwtUtil;
	
	@GetMapping("/recommended/list")
	public ResponseEntity<?> getProposalList(@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		List<GetProposalResponseDto> proposalList = scheduleService.getProposalList(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "추천된 장소 리스트 조회에 성공했습니다.", proposalList));
	}
	
	@PostMapping("/recommend")
	public ResponseEntity<?> addProposal(@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId,
			@RequestBody Integer attractionNo){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		scheduleService.addScheduleProposal(userId, groupId, attractionNo);
		return ResponseEntity.ok(new CommonResponse<>(true, "장소 추천에 성공했습니다.", null));
	}
	
	@PostMapping("/content")
	public ResponseEntity<?> addContent(@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId,
			@RequestBody AddContentRequestDto addContentRequest){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		scheduleService.addContent(userId, groupId, addContentRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "콘텐츠 생성에 성공했습니다.", null));
	}
	
}
