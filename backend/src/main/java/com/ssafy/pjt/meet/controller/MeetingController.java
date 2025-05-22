package com.ssafy.pjt.meet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.meet.dto.request.AvailableSchedulePostRequest;
import com.ssafy.pjt.meet.dto.request.AvailableScheduleUpdateRequest;
import com.ssafy.pjt.meet.dto.response.AvailableScheduleResponse;
import com.ssafy.pjt.meet.entity.AvailableSchedule;
import com.ssafy.pjt.meet.service.AvailableScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetingController {

	private final AvailableScheduleService sService;

	@Operation(summary = "가능한 일정 등록", description = "유저가 가능한 일정을 등록합니다.")
	@ApiResponse(responseCode = "200", description = "일정 등록 성공")
	@PostMapping("/schedule")
	public ResponseEntity<CommonResponse<AvailableScheduleResponse>> postSchedule(
	        @RequestBody AvailableSchedulePostRequest schedulePostRequest) {

	    CommonResponse<AvailableScheduleResponse> commonResponse = sService.addAvailableSchedule(schedulePostRequest);
	    return ResponseEntity.ok(commonResponse);
	}

	@Operation(summary = "가능한 일정 수정", description = "유저의 가능한 일정을 수정합니다.")
	@ApiResponse(responseCode = "200", description = "일정 수정 성공")
	@PutMapping("/schedule")
	public ResponseEntity<CommonResponse<AvailableScheduleResponse>> putSchedule(
	        @RequestBody AvailableScheduleUpdateRequest scheduleUpdateRequest) {

	    CommonResponse<AvailableScheduleResponse> commonResponse = sService.updateAvailableSchedule(scheduleUpdateRequest);
	    return ResponseEntity.ok(commonResponse);
	}

	@Operation(summary = "가능한 일정 삭제", description = "일정 ID로 유저의 가능한 일정을 삭제합니다.")
	@ApiResponse(responseCode = "200", description = "일정 삭제 성공")
	@DeleteMapping("/schedule/{id}")
	public ResponseEntity<?> deleteSchedule(
	        @Parameter(description = "삭제할 일정 ID", example = "1") @PathVariable("id") Integer availableScheduleId) {

		sService.deleteAvailableScheduleById(availableScheduleId);
	    return ResponseEntity.ok(new CommonResponse<>(true, "삭제 성공", null));
	}

	@Operation(summary = "유저의 가능한 일정 조회", description = "특정 그룹 내 유저의 가능한 일정을 모두 조회합니다.")
	@ApiResponse(responseCode = "200", description = "유저 일정 조회 성공")
	@GetMapping("/schedule/{groupId}")
	public ResponseEntity<?> getAllUserSchedule(
	        @Parameter(description = "그룹 ID", example = "101") @PathVariable Integer groupId,
	        @Parameter(description = "유저 ID", example = "ssafy123") @RequestParam String userId) {

		CommonResponse<List<AvailableScheduleResponse>> getResponse = sService.findAvailableScheduleOfUser(userId, groupId);
	    return ResponseEntity.ok(getResponse);
	}

	@Operation(summary = "그룹 전체 가능한 일정 조회", description = "그룹 내 모든 유저의 가능한 일정을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "그룹 일정 조회 성공")
	@GetMapping("/schedule/all/{groupId}")
	public ResponseEntity<?> getAllSchedule(
	        @Parameter(description = "그룹 ID", example = "101") @PathVariable Integer groupId) {

		CommonResponse<List<AvailableSchedule>> getResponse = sService.findAvailableScheduleOfGroup(groupId);
	    return ResponseEntity.ok(getResponse);
	}
}
