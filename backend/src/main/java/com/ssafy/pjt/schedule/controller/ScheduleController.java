package com.ssafy.pjt.schedule.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateScheduleRequestWrapperDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;
import com.ssafy.pjt.schedule.dto.response.GetScheduleResponseDto;
import com.ssafy.pjt.schedule.service.ScheduleService;
import com.ssafy.pjt.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/{groupId}")
public class ScheduleController {

	private final ScheduleService scheduleService;
	private final JwtUtil jwtUtil;
	
	@Operation(summary="추천된 장소 리스트 조회", description="그룹원들이 추천한 장소 리스트를 조회합니다.")
	@ApiResponse(responseCode = "200", description="추천 장소 리스트 조회 성공")
	@GetMapping("/recommended/list")
	public ResponseEntity<?> getProposalList(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId){
		
		String userId = userDetails.getUsername();
		
		List<GetProposalResponseDto> proposalList = scheduleService.getProposalList(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "추천된 장소 리스트 조회에 성공했습니다.", proposalList));
	}
	
	@Operation(summary="추천된 장소 좋아요 토글", description="추천되어있는 장소에 좋아요를 누르지 않은 상태라면 좋아요, 누른 상태라면 좋아요 취소.")
	@ApiResponse(responseCode = "200", description="추천 장소 좋아요 토글 성공")
	@PostMapping("/recommended/like")
	public ResponseEntity<?> likeProposal(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId,
			@RequestBody Integer proposalId){

		String userId = userDetails.getUsername();
		
		if(scheduleService.likeProposal(userId, groupId, proposalId)) {
			return ResponseEntity.ok(new CommonResponse<>(true, "추천된 장소 좋아요 누르기에 성공했습니다.", null));
		}else {
			return ResponseEntity.ok(new CommonResponse<>(true, "추천된 장소 좋아요 취소에 성공했습니다.", null));
		}
		
		
	}
	
	@Operation(summary="장소 추천", description="새로운 장소를 추천합니다.")
	@ApiResponse(responseCode = "200", description="장소 추천 성공")
	@PostMapping("/recommend")
	public ResponseEntity<?> addProposal(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId,
			@RequestBody Integer attractionNo){

		String userId = userDetails.getUsername();
		
		Integer newProposalId = scheduleService.addScheduleProposal(userId, groupId, attractionNo);
		return ResponseEntity.ok(new CommonResponse<>(true, "장소 추천에 성공했습니다.", Map.of("proposalId", newProposalId)));
	}
	
	@Operation(summary="일정 카드 생성", description="새로운 일정 카드를 생성합니다.")
	@ApiResponse(responseCode = "200", description="일정 카드 생성 성공")
	@PostMapping("/content")
	public ResponseEntity<?> addContent(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId,
			@Valid @RequestBody AddContentRequestDto addContentRequest){

		String userId = userDetails.getUsername();
		
		Integer newContentId = scheduleService.addContent(userId, groupId, addContentRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "콘텐츠 생성에 성공했습니다.", Map.of("contentId", newContentId)));
	}
	
	@Operation(summary="일정 카드 수정", description="일정 카드를 수정합니다.")
	@ApiResponse(responseCode = "200", description="일정 카드 수정 성공")
	@PutMapping("/content")
	public ResponseEntity<?> updateContent(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId,
		 	@Valid @RequestBody UpdateContentRequestDto updateContentRequest){

		String userId = userDetails.getUsername();
		
		scheduleService.updateContent(userId, groupId, updateContentRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "콘텐츠 수정에 성공했습니다.", null));
	}
	
	@Operation(summary="일정 카드 삭제", description="일정 카드를 삭제합니다.")
	@ApiResponse(responseCode = "200", description="일정 카드 삭제 성공")
	@DeleteMapping("/content/{contentId}")
	public ResponseEntity<?> deleteContent(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId,
			@PathVariable Integer contentId){

		String userId = userDetails.getUsername();
		
		scheduleService.deleteContent(userId, groupId, contentId);
		return ResponseEntity.ok(new CommonResponse<>(true, "콘텐츠 삭제에 성공했습니다.", null));
	}
	
	@Operation(summary="일정 카드 리스트 조회", description="생성한 일정카드를 모두 조회합니다.")
	@ApiResponse(responseCode = "200", description="일정 카드 리스트 조회 성공")
	@GetMapping("/all")
	public ResponseEntity<?> getScheduleList(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId){

		String userId = userDetails.getUsername();
		
		List<GetScheduleResponseDto> scheduleList = scheduleService.getScheduleList(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "스케줄 조회에 성공했습니다.", scheduleList));
	}
	
	@Operation(summary="일정 업데이트", description="일정의 순서, 또는 공식/비공식 여부를 업데이트합니다.")
	@ApiResponse(responseCode = "200", description="일정 업데이트 성공")
	@PostMapping("/update")
	public ResponseEntity<?> updateSchedule(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer groupId,
			@Valid @RequestBody UpdateScheduleRequestWrapperDto updateScheduleRequest){

		String userId = userDetails.getUsername();
		
		scheduleService.updateSchedules(userId, groupId, updateScheduleRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "스케줄 업데이트에 성공했습니다.", null));
	}
	
	
}
