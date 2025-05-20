package com.ssafy.pjt.schedule.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateScheduleRequestWrapperDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;
import com.ssafy.pjt.schedule.dto.response.GetScheduleResponseDto;
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
	
	@PostMapping("/recommended/like")
	public ResponseEntity<?> likeProposal(@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId,
			@RequestBody Integer proposalId){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		scheduleService.likeProposal(userId, groupId, proposalId);
		return ResponseEntity.ok(new CommonResponse<>(true, "추천된 장소 좋아요 누르기에 성공했습니다.", null));
	}
	
	
	@PostMapping("/recommend")
	public ResponseEntity<?> addProposal(@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId,
			@RequestBody Integer attractionNo){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		Integer newProposalId = scheduleService.addScheduleProposal(userId, groupId, attractionNo);
		return ResponseEntity.ok(new CommonResponse<>(true, "장소 추천에 성공했습니다.", Map.of("proposalId", newProposalId)));
	}
	
	@PostMapping("/content")
	public ResponseEntity<?> addContent(@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId,
			@RequestBody AddContentRequestDto addContentRequest){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		Integer newContentId = scheduleService.addContent(userId, groupId, addContentRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "콘텐츠 생성에 성공했습니다.", Map.of("contentId", newContentId)));
	}
	
	@PutMapping("/content")
	public ResponseEntity<?> updateContent(@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId,
			@RequestBody UpdateContentRequestDto updateContentRequest){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		scheduleService.updateContent(userId, groupId, updateContentRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "콘텐츠 수정에 성공했습니다.", null));
	}
	
	@DeleteMapping("/content/{contentId}")
	public ResponseEntity<?> deleteContent(@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId,
			@PathVariable Integer contentId){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		scheduleService.deleteContent(userId, groupId, contentId);
		return ResponseEntity.ok(new CommonResponse<>(true, "콘텐츠 삭제에 성공했습니다.", null));
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getScheduleList(@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		List<GetScheduleResponseDto> scheduleList = scheduleService.getScheduleList(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "스케줄 조회에 성공했습니다.", scheduleList));
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateSchedule(@RequestHeader("Authorization") String token,
			@PathVariable Integer groupId,
			@RequestBody UpdateScheduleRequestWrapperDto updateScheduleRequest){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		scheduleService.updateSchedules(userId, groupId, updateScheduleRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "스케줄 업데이트에 성공했습니다.", null));
	}
	
	
}
